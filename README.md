# Clan Roster Purifier

![Clan Icon](https://www.champs.chat/wp-content/uploads/2020/04/Clan_Homes_transparent.png) The clan roster purifier plugin takes a truthful source of a clan extract and informs the clan owner what actions they must take to match the truthful source. The clan roster source file is a file which contains an array of objects which each have a 'rsn' value and a 'rank' value. This file acts as the truthful roster, and this plugin tells you how to match that file.

  - Note: Currently, the only supported format is JSON.

# Why?

  - By having an external source tell you the real clan roster exists, this allows you to integrate your RuneScape clan roster with any other service. 
  > For example, a discord bot could be capable of managing the clan roster and matching player roles in discord to their ingame ranks. The discord bot would provide an endpoint for this plugin in JSON. This would be one way of linking discord roles to ingame ranks. if there are any discrepencies, the next time the clan owner logs in, the owner can correct the ingame rank discrepencies with this nifty plugin.


# Setup
---
  - The settings of the plugin are as follows. (Only JSON is currently supported)
   ![Settings](http://champs.chat/images/2020-04-20_20:38:29PM.png)

    | Accepted Formats | Example |
    | ------ | ------ |
    | JSON | [{"rsn":"Anatamize","rank":"General"}, {"rsn":"Profiteer","rank":"Friend"}] |

  - The format you choose should match your roster data. (Once again, JSON...)
   ![Data](http://champs.chat/images/2020-04-20_20:46:09PM.png)

# Usage
---
  - Turn on the plugin and fill out the configuration.
  - Log into your RuneScape account.
  - If you see "Input data is malformed/corrupt", visit Setup above. Your formatting or URI is incorrect. In the following picture I accidentally put a '2' at the end of my Clan Roster URI.
  ![Input URI is malformed/corrupt](http://champs.chat/images/2020-04-20_20:42:04PM.png)
  - The plugin will tell you to visit your clan setup so it can extract your clan's current ranks.
  ![Visit 'Clan Setup'](http://champs.chat/images/2020-04-20_20:41:52PM.png)
  - Once you visit the 'Clan Setup', you will be shown the actions you need to match your truthful clan roster URI data. That's it!
  ![Clan Roster Actions](http://champs.chat/images/2020-04-20_20:40:49PM.png)
  - Once you match it up, there will be no actions left! Success!
  ![Clan Roster Actions](http://champs.chat/images/2020-04-20_20:40:49PM.png)
 ![Success](http://champs.chat/images/2020-04-20_20:41:43PM.png)
  - Profit
