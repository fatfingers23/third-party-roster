/*
 * Copyright (c) 2021, bluelightzero
 * Copyright (c) 2020, Spencer Imbleau <spencer@imbleau.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.clanrosterhelper;

import com.google.common.base.Strings;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.http.api.RuneLiteAPI;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@PluginDescriptor(
        name = "Clan Roster Helper",
        description = "Informs the user of actions to match a truthful copy of the clan roster",
        tags = {"clan", "roster", "helper"}
)
@Slf4j
public class ClanRosterHelperPlugin extends Plugin {

    @Inject
    private Client client;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private ClanRosterHelperOverlay overlay;

    @Inject
    private ClanRosterHelperConfig config;

    /**
     * Whether the config URI for the clan roster is loaded and valid
     */
    private boolean isClanRosterCorrupt = true;

    /**
     * The valid copy of the clan roster to compare your clan setup to
     */
    private ClanRosterTruth clanRosterTruth = null;

    /**
     * The clan members, scraped from your clan setup widget
     */
    private List<ClanMemberMap> clanMembers = null;

    /**
     * Whether the clan setup widget is visible
     */
    private boolean isClanSetupWidgetAvailable = false;

    @Provides
    ClanRosterHelperConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(ClanRosterHelperConfig.class);
    }

    @Override
    public void startUp() {
        overlayManager.add(overlay);
    }

    @Override
    public void shutDown() {
        overlayManager.remove(overlay);
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        //Download and digest the truthful clan roster via config

        if (Strings.isNullOrEmpty(config.getDataUrl())) {
            clanRosterTruth = null;
            isClanRosterCorrupt = true;
            overlay.update();
            return;
        }

        final Request request = new Request.Builder()
                .url(config.getDataUrl())
                .build();

        RuneLiteAPI.CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                clanRosterTruth = null;
                isClanRosterCorrupt = true;
                overlay.update();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final String source = response.body().string();
                    digestClanRoster(source);
                    overlay.update();
                } finally {
                    response.close();
                }
            }
        });
    }

    @Subscribe
    public void onWidgetLoaded(WidgetLoaded widget) {
        if (widget.getGroupId() == 94) {
            this.isClanSetupWidgetAvailable = true;
        }
    }

    @Subscribe
    public void onGameTick(GameTick gameTick) {
        //Update the overlay if the clan setup widget is visible on screen
        if (this.isClanSetupWidgetAvailable) {
            if (this.client.getWidget(94, 28) == null) {
                this.clanMembers = null;
                this.isClanSetupWidgetAvailable = false;
            } else {
                scrapeMembers();
            }
            overlay.update();
        }
    }

    private String cleanColor(String input) {
        return input.replaceAll("<[^>]*>", "");
    }

    private String replaceNonBreakingSpaces(String input) {
        return input.replaceAll(" ", " ");
    }

    private boolean isRank(String menuRank) {
        switch (menuRank) {
            case "Not ranked": return true;
            case "Recruit": return true;
            case "Corporal": return true;
            case "Sergeant": return true;
            case "Lieutenant": return true;
            case "Captain": return true;
            case "General": return true;
            default: return false;
        }
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event)
    {
        MenuEntry[] menuEntries = client.getMenuEntries();

        for (MenuEntry menuEntry : menuEntries) {
            // RSNs have non-breaking spaces instead of regular ones in menus.
            String rsnMenu = cleanColor(menuEntry.getTarget());
            String rsn = replaceNonBreakingSpaces(rsnMenu);

            String menuRank = menuEntry.getOption();

            if (isRank(menuRank)) {

                String expectedRank = "Not in clan";
                String currentRank = "Not ranked";

                if (clanRosterTruth != null)
                    for (ClanMemberMap clanMemberMap : clanRosterTruth.MEMBERS) {
                        if (rsn.equalsIgnoreCase(clanMemberMap.getRSN())) {
                            expectedRank = clanMemberMap.getRank();
                            break;
                        }
                    }

                if (clanMembers != null)
                    for (ClanMemberMap clanMemberMap : clanMembers) {
                        if (rsn.equalsIgnoreCase(clanMemberMap.getRSN())) {
                            currentRank = clanMemberMap.getRank();
                            break;
                        }
                    }

                Color highlight;

                if (expectedRank.equals("Not in clan")) {
                    if (menuRank.equals("Not ranked")) {
                        if (menuRank.equals(currentRank)) {
                            highlight = Color.GREEN;
                        } else {
                            highlight = Color.YELLOW;
                        }
                    } else if (menuRank.equals(currentRank)) {
                        highlight = Color.RED;
                    } else {
                        highlight = Color.BLACK;
                    }
                } else if (expectedRank.equals(currentRank)) {
                    if (menuRank.equals(currentRank)) {
                        highlight = Color.GREEN;
                    } else {
                        highlight = Color.gray;
                    }
                } else {
                    if (menuRank.equals(currentRank)) {
                        highlight = Color.RED;
                    } else if (menuRank.equals(expectedRank)) {
                        highlight = Color.YELLOW;
                    } else {
                        highlight = Color.gray;
                    }
                }

                menuEntry.setTarget(ColorUtil.prependColorTag(rsnMenu, highlight));
            }
        }

        client.setMenuEntries(menuEntries);
    }

    /**
     * @return the truthful copy of the clan roster
     */
    public @Nullable
    ClanRosterTruth getClanRosterTruth() {
        return this.clanRosterTruth;
    }

    /**
     * @return the clan members from the clan roster widget
     */
    public @Nullable
    List<ClanMemberMap> getClanMembers() {
        return this.clanMembers;
    }

    /**
     * @return the client
     */
    public Client getClient() {
        return this.client;
    }

    /**
     * @return whether the clan roster is corrupt
     */
    public boolean isClanRosterCorrupt() {
        return this.isClanRosterCorrupt;
    }


    // SUB ROUTINES BELOW

    /**
     * Subroutine - Digest the valid copy of the clan roster from source
     *
     * @param source - the source of the clan roster
     * @return whether the clan roster is valid
     */
    private boolean digestClanRoster(final String source) {
        try {
            switch (config.getDataInputFormat()) {
                case JSON:
                    clanRosterTruth = ClanRosterTruth.fromJSON(source);
                    isClanRosterCorrupt = false;
                    break;
                default:
                    clanRosterTruth = null;
                    isClanRosterCorrupt = true;
                    break;
            }
        } catch (Exception e) {
            clanRosterTruth = null;
            isClanRosterCorrupt = true;
        }

        return isClanRosterCorrupt;
    }

    /**
     * Subroutine - Update our memory of clan members and their ranks for
     * clan setup
     */
    public void scrapeMembers() {
        if (this.clanMembers == null) {
            this.clanMembers = new ArrayList<>();
        }
        this.clanMembers.clear();

        //Scrape all clan members
        Widget memberContainer = this.client.getWidget(94, 28);
        Widget[] memberValues = memberContainer.getChildren();
        if (memberValues != null) {
            int members = memberValues.length / 4;
            for (int i = 0; i < members; i++) {
                String rsn = memberValues[i * 4 + 2].getText();
                String currentRank = cleanColor(memberValues[i * 4 + 1].getText());

                String expectedRank = "Not in clan";

                if(clanRosterTruth != null)
                    for(ClanMemberMap clanMemberMap : clanRosterTruth.MEMBERS) {
                        if(rsn.equalsIgnoreCase(clanMemberMap.getRSN())) {
                            expectedRank = clanMemberMap.getRank();
                            break;
                        }
                    }

                Color highlight;
                if(expectedRank.equals("Not in clan") && currentRank.equals("Not ranked")) {
                    highlight = Color.BLACK;
                } else if(expectedRank.equals(currentRank)) {
                    highlight = Color.GREEN;
                } else {
                    highlight = Color.RED;
                }

                memberValues[i * 4 + 1].setText(ColorUtil.prependColorTag(currentRank, highlight));
                ClanMemberMap clanMember = new ClanMemberMap(rsn, currentRank);
                this.clanMembers.add(clanMember);
            }
        }
    }
}
