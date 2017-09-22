USAGE

Import unity package, remember to import only newest .aar and .jar file to prevent merging java classes errors when building .apk package.

Add <receiver android:name="com.gamesture.gamesturelocalnotifications.NotificationsManager"/> to AndroidManifest.xml to be able to recive notifications after application is killed and is not working in background

NOTE res/ folders are obsolete but in 5.4 unity are still working, but .aar compiled plugin is not able to be build in Unity Editor. Untill new version to change icons replace them in res/ folder placed in plugins.
