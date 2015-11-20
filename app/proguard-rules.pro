# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /crucial/android-sdk/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-optimizationpasses 1
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keepattributes EnclosingMethod,Signature,*Annotation*,SourceFile,LineNumberTable,Exceptions,InnerClasses

-allowaccessmodification
-renamesourcefileattribute SourceFile
-repackageclasses ''

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Preserve all native method names and the names of their classes.
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#################

#-dontwarn java.awt.**

-dontwarn org.codehaus.plexus.**
-dontwarn org.jsonschema2pojo.gradle.**
-dontwarn com.fasterxml.jackson.databind.ext.**
-dontwarn com.sun.codemodel.util.**
-dontwarn edu.emory.mathcs.backport.**
-dontwarn org.joda.time.**
-dontwarn org.apache.maven.shared.utils.**
-dontwarn org.codehaus.jackson.map.**
-dontwarn org.jsonschema2pojo.util.NameHelper

-keep class com.azavea.prs.** { *; }

-keep public class org.codehaus.** { *; }
-keep interface org.codehaus.** { *; }
-keep class hidden.org.codehaus.plexus.interpolation.** { *; }
-keep interface hidden.org.codehaus.plexus.interpolation.** { *; }

-keep class groovy.lang.** { public *; }
-keep interface groovy.lang.** { publci *; }

-keep class org.gradle.api.** { public *; }
-keep interface org.gradle.api.** { public *; }

-keep class org.w3c.dom.** { public *; }
-keep class javax.lang.model.** { public *; }
-keep class java.beans.** { *; }
-keep class sun.nio.css.** { *; }
-keep class sun.misc.** { *; }
-keep class org.apache.maven.toolchain.** { public *; }
-keep class org.joda.convert.** { public *; }
-keep class org.codehaus.groovy.runtime.** { public *; }
-keep class java.rmi.** { public *; }
-keep class com.sun.tools.** { public *; }
-keep class sun.rmi.rmic.** { public *; }
-keep class weblogic.rmic.** { public *; }

-keep public class java.awt.** { public *; }
-keep interface java.awt.** { *; }
-keep public class org.codehaus.** { public *; }
-keep interface org.codehaus.** { *; }

-keep class org.apache.tools.ant.** { public *; }
-keep class javax.activation.** { public *; }
-keep class javax.mail.internet.** { public *; }
-keep class java.nio.** { public *; }
-keep class gnu.gcj.** { public *; }

-keepclassmembers class *.* {
    @org.codehaus.plexus.util.interpolation.** <methods>;
    @hidden.org.codehaus.plexus.interpolation.** <methods>;
}

-keep, includedescriptorclasses public class *.* {
    public protected <fields>;
    public protected <methods>;
    public protected *;
}

