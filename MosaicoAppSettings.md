# Project Settings #

When we create a new Android Project we must specify certain things before start programming :P so here is one suggestion. We must all agree on one specific set of values.

## Eclipse Project ##
| **Project Name** | Mosaico |
|:-----------------|:--------|
| **Build Target** | Android 2.2 |
| **Application Name** | Mosaico |
| **Package Name** | `pt.up.fe.android.mosaico` |
| **Activity** | <we should choose our activites (screens)> |
| **Min SDK Version** | 8 |
| **Public App key** | make sure everybody uses the same |
| **Google Maps key** | make sure everybody uses the same |

## API Key ##
Please read: http://chrislee.kr/wp/2010/12/31/share-google-android-api-key-in-eclipse-with-other-team-members/

## Screens & Menus ##
**NB!** _for Android every **screen** is an **activity**, every activity is a different **class**! These words will be used as synonyms._

### Screens ###

  1. **Main Screen** - The screen with the sliding grid of all photos
  1. **Photo Screen** - The screen where the selected photo will be show

### Menus ###
  1. **Main menu** - will appear in the **Main Screen**
    * Options: | Check here! | Favorites | History | Settings |
|:------------|:----------|:--------|:---------|
      * **Check here!** - Check another gps location (takes you to Google Maps to point at a different location).
      * **Favorites** -
      * **History** -
      * **Settings** - This will be a Preference menu. _see Tutorials_
  1. **Photo Menu** -