## Friend's Circle - Open social media application

### Implementation:
A social media application to share text, images and to stalk posts of other users   
  
**APK Link:**    
Download from [here](https://drive.neptunelabs.xyz/s/SocialMediaApp/download?path=%2F&files=Circle.apk)   
Screens here [here](https://drive.neptunelabs.xyz/s/SocialMediaApp)   

### Project Breakdown
**Project Architecture:**  
Clean - MVVM

**Project Language:**  
Kotlin  

**Project Highlights:**   
1. Focussed on SOLID principles | Clean - MVVM Architecture
2. Object oriented programming approach
3. DI using Hilt
4. Usage of Jetpack components such as Room, NavGraph, View/DataBinding, ViewModel, LiveData...
5. Kotlin Coroutines/Flow, Rx
6. WorkManager demonstrating timeline multipart POST requests
7. Firebase Firestore network layer
8. Optional Obfuscation
9. Scalability

**Database used:**   
[Room](https://developer.android.com/jetpack/androidx/releases/room)

**SCM/Git:**   
[GitHub](https://github.com/njarun/MiCircle)

### Screens
1. Splash page
2. Login page
3. Registration page
4. Dashboard page with Home and Profile
5. New Post Page
6. ImageViewer Page

### Application flow:
1. When the app is launched, the Splash page check's for the user status, if the user is valid the Dashboard is opened, else Login page
2. From the login page, the user can login using email/password or move to registration page
3. In registration page, and once the registration is completed the user is taken to the Dashboard page
4. Dashboard page holds the public feed listings and the user profile fragments
5. In the public feed listing, the feeds of all users are fetched from the server with pagination also the new post page is opened with the FAB
6. The first batch of the feeds are saved to database for offline access, and its loaded to UI next time app is launched
7. In the profile page, the feeds of the user is listed along with the logout option
8. In the New posts page, the currently logged in user can share text and/or images from galley to the timeline 
9. If the user is offline or the post has media, then the WorkManager is scheduled to share the post to the timeline

### Capabilities
1. Clean-MVVM architecture, components are easily replaceable and focussed on SOLID principles
2. Rx to pass events/messages to parts of application
3. The Dashboard transition is with NavGraph component
4. UI is loaded by View/Data binding
5. Functional/Method bindings for views
6. Factory class structure for Local/Network data sources
7. Room database is updated with API results for persistence
8. Hilt for dependency injection
9. Work manager support for background media upload
10. Day/Night theme support

### Screenshots:
  
**Splash Page:**  
  
<img src="https://drive.neptunelabs.xyz/s/SocialMediaApp/download?path=%2F&files=1_Splash.png" width="360" height="780"> 
  
**Login Page:**  
  
<img src="https://drive.neptunelabs.xyz/s/SocialMediaApp/download?path=%2F&files=2_Login.png" width="360" height="780"> 
  
**Registration Page:**  
  
<img src="https://drive.neptunelabs.xyz/s/SocialMediaApp/download?path=%2F&files=3_Registration.png" width="360" height="780">  
  
**Home Page:**  
  
<img src="https://drive.neptunelabs.xyz/s/SocialMediaApp/download?path=%2F&files=4_Home.png" width="360" height="780">  
  
**Profile Page:**  
  
<img src="https://drive.neptunelabs.xyz/s/SocialMediaApp/download?path=%2F&files=5_Profile.png" width="360" height="780">  
  
**New Post Page:**  
  
<img src="https://drive.neptunelabs.xyz/s/SocialMediaApp/download?path=%2F&files=6_NewPost.png" width="360" height="780">  
  
**NewPost Upload Notification:**  
  
<img src="https://drive.neptunelabs.xyz/s/SocialMediaApp/download?path=%2F&files=7_NewPost_Upload_Notification.png" width="360" height="780">  
