# TaintDroid

* Computer Security SNU
* Fall 2015
* Project 4. Information Flow Tracking in Android
* 김찬민, 심규민

## 컴파일 방법

http://appanalysis.org/download.html 이 instruction을 따라 하되,
`.repo/local_manifests/local_manifest.xml` 파일의 다음 부분을 수정해준다.

```
  <project path="dalvik" remote="github" name="sim0629/taintdroid" revision="android_platform_dalvik"/>
  <project path="libcore" remote="github" name="sim0629/taintdroid" revision="android_platform_libcore"/>
  <project path="frameworks/base" remote="github" name="sim0629/taintdroid" revision="android_platform_frameworks_base"/>
  <project path="packages/apps/TaintDroidNotify" remote="github" name="sim0629/taintdroid" revision="android_platform_packages_apps_TaintDroidNotify"/>
```

