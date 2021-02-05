> ## Important Information
> This repository can viewed as a regular github [repository](https://github.com/numericalalgorithmsgroup/NAGJavaExamples/) or as a [webpage](https://numericalalgorithmsgroup.github.io/NAGJavaExamples/).

# Examples using the NAG Library for Java

This repository contains examples and demonstrations using the [NAG Library for Java](https://www.nag.com/content/nag-library-for-java).  The NAG Library for Java contains 1900+ functions spanning many areas of numerical computing and data science.

## Directory of GitHub examples

* [Nearest Correlation Matrices](./nearest_correlation_matrices)
* [Quadratically constrained quadratic programming and its applications in portfolio optimization](./QCQP)

## Examples that ship with the product

The NAG Library for Java ships with a set of usage [examples](https://github.com/numericalalgorithmsgroup/NAGJavaExamples/tree/main/simple_examples).

## Prerequisites

The NAG Library for Java is NOT compatible with all implementations of the NAG Library. It is therefore important that you ensure that the correct implementation of the NAG Library is installed in order to use these wrappers.

The NAG Library for Java Mark 27.1 is available for the following platforms:

| Platform | NAG Library |
| --- | --- |
| Linux 64-bit | NLL6I271BL |
| Windows 64-bit | NLW6I271EL |

You can get the NAG Library from [here](https://www.nag.com/content/nag-library).

## Obtaining a license

Before you can use the NAG Library for Java, you'll need a license. Free trial licenses are available!

To request a licence key, you first need to know your Kusari *hostid* and email that along with the *Product Code* to [support@nag.com](mailto:support@nag.com).

To get your *khostid*:

### Linux

* Run the program *khostid* located in the *license/bin/linux_x64* subdirectory of the software distribution.

* Once you have obtained one of the above licence keys, the easiest way to install it is to store the text in a file,
```
$HOME/nag.key
```
or
```
/opt/NAG/nag.key
```
or
```
/usr/local/NAG/nag.key
```

### Windows

* Use the Kusari Installer GUI. Once you have a key, the same GUI can be used to install it.

More detailed installation instructions are availavle in the [Installer's Note](https://www.nag.com/content/nag-library-mark-27).

## NAG Library for Java installation

You can get the NAG Library for Java from [here](https://www.nag.com/content/nag-library-java-download).

### Linux

To install the NAG Library for Java, you simply have to unzip the distribution file and copy two files to convenient locations on your system:

* NAGJava.jar
* libnag_jni271.so

The following **must** be in your *LD_LIBRARY_PATH* environment variable to use the NAG Library for Java:

* The directory containing *libnag_jni271.so*
* The directories *lp64/lib* and *rtl/lib/intel64* of the underlying library NLL6I271BL

It is also recommended that the path to NAGJava.jar is added to your CLASSPATH.

So your *LD_LIBRARY_PATH* should contain something like:

```
[nagjava_install_dir]/linux_x64:[nll6i271bl_install_dir]/lp64/lib:[nll6i271bl_install_dir]/rtl/lib/intel64
```

and your *CLASSPATH*:

```
.:[nagjava_install_dir]/jar/NAGJava.jar
```

If you are using an IDE such as Eclipse, you may need to configure your project to enable the IDE to pick up any required dependency.

### Windows

To install the NAG Library for Java, you simply have to unzip the distribution file and copy two files to convenient locations on your system:

* NAGJava.jar
* nag_jni271.dll

The following **must** be in your *PATH* environment variable to use the NAG Library for Java:

* The directory containing *nag_jni271.dll*
* The directories *bin* and *rtl\bin* of the underlying library NLW6I271EL

It is also recommended that the path to NAGJava.jar is added to your CLASSPATH.

So your *PATH* should contain something like:

```
[nagjava_install_dir]\win64;[nlw6i271el_install_dir]\bin;[nlw6i271el_install_dir]\rtl\bin
```

and your *CLASSPATH*:

```
.;[nagjava_install_dir]\jar\NAGJava.jar
```

If you are using an IDE such as Eclipse, you may need to configure your project to enable the IDE to pick up any required dependency.

