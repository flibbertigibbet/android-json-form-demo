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

-optimizationpasses 3
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt

-keepattributes EnclosingMethod,Signature,*Annotation*,SourceFile,LineNumberTable,Exceptions,InnerClasses,Deprecated

# http://proguard.sourceforge.net/manual/examples.html#beans
-adaptresourcefilenames    **.properties,**.gif,**.jpg
-adaptresourcefilecontents **.properties,META-INF/MANIFEST.MF

-allowaccessmodification
-renamesourcefileattribute SourceFile
-repackageclasses ''

-keep class com.azavea.prs.** { *; }

# keep annotation classes so they do not get obfuscated, even if not referenced directly
-keep class org.jsonschema2pojo.annotations.** { *; }
-keep class com.fasterxml.jackson.annotation.** { *; }
-keep class com.google.gson.annotations.** { *; }
-keep class javax.annotation.** { *; }

# fix remaining warnings
-keep class com.google.vending.licensing.ILicensingService { *; }
-keep class org.w3c.dom.** { *; }
-keep class sun.nio.cs.** { *; }
-keep class javax.lang.model.** { *; }

# warnings re: dynamic references
-keep class libcore.icu.** { *; }
-keep class android.graphics.** { *; }

-dontwarn org.w3c.dom.**
-dontwarn sun.nio.cs.**
-dontwarn javax.lang.model.**
#-dontwarn com.google.common.**

-keepclassmembers class * {
    @javax.annotation.Resource *;
    @org.springframework.beans.factory.annotation.Autowired *;
    @android.webkit.JavascriptInterface <methods>;
    @org.codehaus.jackson.annotate.* <fields>;
    @org.codehaus.jackson.annotate.* <init>(...);
}

####################
# below configurations based on:
# https://github.com/krschultz/android-proguard-snippets

## Joda Time 2.3

-dontwarn org.joda.convert.**
-dontwarn org.joda.time.**
-keep class org.joda.time.** { *; }
-keep interface org.joda.time.** { *; }

## Joda Convert 1.6

-keep class org.joda.convert.** { *; }
-keep interface org.joda.convert.** { *; }

# support design

-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

-keep public class * extends android.support.design.widget.** {
    public <init>(android.content.Context, android.util.AttributeSet);
}

# app compat v7

-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

## GSON 2.2.4 specific rules ##

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }


# Proguard configuration for Jackson 2.x (fasterxml package instead of codehaus package)
-keep class com.fasterxml.jackson.databind.ObjectMapper {
    public <methods>;
    protected <methods>;
}
-keep class com.fasterxml.jackson.databind.ObjectWriter {
    public ** writeValueAsString(**);
}

#######
# ignore optional bval deps
#-dontwarn javax.xml.bind.**
#-dontwarn org.osgi.**
#-dontwarn org.apache.bval.xml.**
#-dontwarn org.apache.commons.weaver.**
#-dontwarn org.apache.bval.util.**
#-dontwarn org.apache.commons.beanutils.**
#-dontwarn org.apache.geronimo.**
#-dontwarn org.apache.bval.jsr303.util.SecureActions

# missing stuff on android
-dontwarn java.beans.**
-dontwarn java.lang.instrument.**
-dontwarn java.lang.reflect.**
-dontwarn java.lang.Object


-dontwarn javax.swing.**
#-dontwarn net.sf.cglib.**
#-dontwarn org.osgi.**
-dontwarn java.awt.**

-dontwarn com.sun.xml.**
-dontwarn org.jdom.**
-dontwarn com.sun.msv.**
-dontwarn java.xml.transform.**
-dontwarn org.dom4j.swing.**

-dontwarn org.apache.bcel.verifier.** # uses awt dialogs

-dontwarn sun.misc.Unsafe
-dontwarn java.applet.**
-dontwarn org.slf4j.**
-dontwarn javax.naming
-dontwarn org.w3c.dom.**

-dontwarn com.sun.org.apache.xerces.**
-dontwarn org.apache.bval.jsr303.util.SecureActions
-dontwarn org.apache.commons.beanutils.MappedPropertyDescriptor
-dontwarn javax.xml.**
-dontwarn javax.naming.**
-dontwarn java.lang.**
-dontwarn org.eclipse.osgi.**
-dontwarn nu.xom.**
-dontwarn org.apache.bval.util.**
-dontwarn com.sun.activiation.**
-dontwarn com.bea.xml.**
-dontwarn org.kxml2.**
-dontwarn org.xmlpull.**
-dontwarn com.sun.activation.**
-dontwarn org.apache.geronimo.osgi.**
-dontwarn com.ctc.wstx.stax.**
-dontwarn javax.activation.**
-dontwarn org.xml.sax.**