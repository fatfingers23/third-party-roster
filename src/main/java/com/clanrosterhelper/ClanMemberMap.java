/*
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
package net.runelite.client.plugins.clanrosterhelper;

/**
 * A simple mapping of RSN -> Rank
 */
public class ClanMemberMap {

    /**
     * The runescpae player's name
     */
    private String rsn;

    /**
     * The runescape player's rank
     */
    private String rank;

    /**
     *  Date the runscape player's joined the clan
     */
    private String joinedDate;

    /**
     * Initialize a map from runescape player name to rank
     *
     * @param rsn  - the player name
     * @param rank - the player rank
     * @param joinedDate - date player joined the clan
     */
    public ClanMemberMap(String rsn, String rank, String joinedDate) {
        this.rsn = rsn;
        this.rank = rank;
        this.joinedDate = joinedDate;
    }

    /**
     * @return the runescape player's name
     */
    public String getRSN() {
        return this.rsn;
    }

    /**
     * @return the runescape player's rank
     */
    public String getRank() {
        return this.rank;
    }

    /**
     * @return the runescape player's joined date
     */
    public String getJoinedDate() { return  this.joinedDate;}
}
