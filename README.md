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
