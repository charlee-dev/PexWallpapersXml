# Welcome to PexWallpapers!

Hi! PexWallpapers is one of my current projects. The app use Pexels.com image library to show
pictures/wallpapers in the app. You can browse different categories or just use **Search** to find
some beautiful wallpapers for your phone. If you add wallpapers to favourites than you can turn
on **Auto Wallpaper Setter** in Settings, to have your phone wallpaper changed every specific period
of time.

# Setup

To run app:

1. git clone repo to Android studio
2. Get your own Api Key from [Pexels.com](https://www.pexels.com/api/)
3. Add your own Api Key to gradle.properties in this format

   pex_api_access_key="234f9170000324234012343d044b1a3482ba588"

4. And run in emulator or on physical device

## Stack

- ***MVVM*** architecture
- ***Retrofit*** for pulling data from network
- ***Room*** for persistence
- ***Coroutines*** for threading
- All *Retrofit* and *Room* logic handled in *Repository*
- *ViewModel* handles logic between UI and *Repository* using **Flows** and **Coroutines**
- Using ****Coil**** for fetching images from web
- **Hilt** for dependency injection
- **Paging 3** for displaying long Wallpaper lists from web
- **Navigation Component**  for navigation in an app ofc
- Data Binding
- **Lottie** is used in a SwipeRefreshLayout
- **Shimmer** effect for while loading Wallpapers
- **UI testing** and **Unit testing** (in progress)

# Screens - Features

## Wallpaper Screen

- Wallpaper Screen shows *Curated Photos* form Pexels.com.
- Images are displayed in **Staggered Recycler View** using **ListView Adapter**.
- **Menu button** lets display menu with one action **Refresh**
- You can **Swipe down** to refresh list too.
- All results are saved in local database.

![PexWallpapers - Wallpapers Screen](https://github.com/adimanwit/PexWallpapers/raw/master/screenshots/wallpapers_screen.png)

## Search Screen

- To display wallpapers in Search screen use **searchView** to search your query.
- When searchView **is focused** it **shows Suggestions** saved in Room database.
- Every new search *adds suggestion* to database.
- Added suggestion have **close button** next to suggestion name.
- Suggestions are displayed in **Horizontal RecyclerView**.
- Suggestions are **filtered** while typing in searchview.
- **Swipe down** to refresh list.
- Search results are **paginated**.
- **Menu button** lets display menu with one action **Refresh**
- All results are saved in local database.

![PexWallpapers - Search screen](https://github.com/adimanwit/PexWallpapers/raw/master/screenshots/search_screen.png)

## Favourites Screen

- Gets favorites from local database
- Instagram style **Double Click to Heart** wallpaper
- **Swipe down** to refresh list.
- **Menu button** lets display menu with one action **Refresh**
- List displayed in *single column* recyclerView

![PexWallpapers - Favorites Screen](https://github.com/adimanwit/PexWallpapers/raw/master/screenshots/favorites_screen.png)

## Preview Screen

- Shows **full screen** preview, with bottom navigation hidden
- At the bottom of screen is **swipe handle**, opening **BottomSheet** with wallpaper actions

![PexWallpapers - Preview Screen](https://github.com/adimanwit/PexWallpapers/raw/master/screenshots/preview_screen.png)

## Preview - Bottom Sheet

- Shows image **thumbnail** and info about photographer
- **Pexels button** witch takes you to Pexels.com website with current picture
- **Share button** lets share picture in *social media, message* etc
- Heart image for **liking/disliking image** witch syncs automatically with local database using **
  Flow**
- **Set Wallpaper** button takes you to next screen
- Horizontal list shows **similar wallpapers** from the same category.

![PexWallpapers - Preview Bottom Sheet](https://github.com/adimanwit/PexWallpapers/blob/master/screenshots/preview_bottom_sheet_screen.png)

# Copyright

```
Copyright 2021 Adrian Witaszak

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
