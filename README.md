[![](https://img.shields.io/endpoint?url=https://i.pluginhub.info/shields/rank/author/Spencer%20Imbleau)](https://runelite.net/plugin-hub) [![](https://img.shields.io/endpoint?url=https://i.pluginhub.info/shields/rank/plugin/clan-roster-helper-plugin)](https://runelite.net/plugin-hub) [![](https://img.shields.io/endpoint?url=https://i.pluginhub.info/shields/installs/plugin/clan-roster-helper-plugin)](https://runelite.net/plugin-hub) 

https://github.com/simbleau/clan-roster-purifier

# State

⚠️ **[DEPRECATED] Not stable or supported since the [clan update](https://secure.runescape.com/m=news/clans-soft-launch?oldschool=1).**

  - This plugin is not actively maintained and probably will cause issues with your client! This is directly related to Jagex's decision to change the UI and clan system entirely. Unless someone rescues this repo and updates it, it will depend on when I get around to it.

# Clan Roster Helper

![Clan Icon](https://imbleau.com/runelite/third-party-roster/icon.png) This plugin takes a third party roster source as the truthful source and informs the RuneLite user how to match that in game with guidance. The guidance comes in the form of overlays telling the RuneLite user what promotions and demotions to apply to match  the given source. The third party source file should be a file which contains an array of objects which each have an 'rsn' value and a 'rank' value.

  - Note: Currently, the only supported format is JSON.

# Why?

  - By having an external source tell you the real clan roster exists, this allows you to integrate your RuneScape clan roster with any other service. 
  > For example, a discord bot could be capable of managing the clan roster and matching player roles in discord to their ingame ranks. The discord bot would provide an endpoint for this plugin in JSON. This would be one way of linking discord roles to ingame ranks. if there are any discrepencies, the next time the clan owner logs in, the owner can correct the ingame rank discrepencies with this nifty plugin.


# Setup
---
  - The settings of the plugin are as follows. (Only JSON is currently supported)

   ![Settings](https://imbleau.com/runelite/third-party-roster/settings.png)

   | Accepted Formats | Example |
   | ------ | ------ |
   | JSON | [{"rsn":"Anatamize","rank":"General"}, {"rsn":"Profiteer","rank":"Friend"}] |

  - The format you choose should match your roster data. (Once again, JSON...)

   ![Data](https://imbleau.com/runelite/third-party-roster/example_input.png)

# Usage
---
  - Turn on the plugin and fill out the configuration.

  - Log into your RuneScape account.

  - If you see "Input data is malformed/corrupt", visit Setup above. Your formatting or URI is incorrect. In the following picture I accidentally put a '2' at the end of my Clan Roster URI.

   ![Input URI is malformed/corrupt](https://imbleau.com/runelite/third-party-roster/input_malformed2.png)

  - The plugin will tell you to visit your clan setup so it can extract your clan's current ranks.

   ![Visit 'Clan Setup'](https://imbleau.com/runelite/third-party-roster/visit_setup.png)

  - Once you visit the 'Clan Setup', you will be shown the actions you need to match your truthful clan roster URI data. That's it!

   ![Clan Roster Actions](https://imbleau.com/runelite/third-party-roster/actions.png)

  - Once you match it up, there will be no actions left! Success!

   ![Success](https://imbleau.com/runelite/third-party-roster/no_actions.png)
