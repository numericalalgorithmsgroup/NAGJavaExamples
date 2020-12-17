![NAG Logo](./nag_logo.png)

# Examples using the NAG Library for Java

This repository contains examples and demonstrations using the [NAG Library for Java](https://www.nag.com/content/nag-library-for-java).  The NAG Library for Java contains 1900+ functions spanning many areas of numerical computing and data science.

## Directory of GitHub examples

* [Nearest Correlation Matrices](./nearest_correlation_matrices)
* [Quadratically constrained quadratic programming and its applications in portfolio optimization]()

## Examples that ship with the product

The NAG Library for Java ships with a set of usage [examples]().

## Prerequisites

The NAG Library for Java is NOT compatible with all implementations of the NAG Library. It is therefore important that you ensure that the correct implementation of the NAG Library is installed in order to use these wrappers.

The NAG Library for Java Mark 27.1 is available for the following platforms:

| Platform | NAG Library |
| --- | --- |
| Linux 64-bit | NLL6I271BL |
| Windows 64-bit | NLW6I271EL |

You can get the NAG Library from [here](https://www.nag.com/content/nag-library).

## NAG Library for Java installation

### Linux

To install the NAG Library for Java, you simply have to unzip the distribution file and copy two files to convenient locations on your system:

* NAGJava.jar
* libnag_jni271.so

The following **must** be in your LD_LIBRARY_PATH environment variable to use the NAG Library for Java:

The directory containing libnag_jni271.so
The directories lp64/lib and rtl/lib/intel64 of the underlying library NLL6I271BL

It is also recommended that the path to NAGJava.jar is added to your CLASSPATH.

So your LD_LIBRARY_PATH should contain something like:

```
[nagjava_install_dir]/linux_x64:[nll6i271bl_install_dir]/lp64/lib:[nll6i271bl_install_dir]/rtl/lib/intel64
```

and your CLASSPATH:

```
.:[nagjava_install_dir]/jar/NAGJava.jar
```

If you are using an IDE such as Eclipse, you may need to configure your project to enable the IDE to pick up any required dependency.

### Windows

To install the NAG Library for Java, you simply have to unzip the distribution file and copy two files to convenient locations on your system:

* NAGJava.jar
* nag_jni271.dll

The following **must** be in your PATH environment variable to use the NAG Library for Java:

The directory containing nag_jni271.dll
The directories bin and rtl\bin of the underlying library NLW6I271EL

It is also recommended that the path to NAGJava.jar is added to your CLASSPATH.

So your PATH should contain something like:

```
[nagjava_install_dir]\win64;[nlw6i271el_install_dir]\bin;[nlw6i271el_install_dir]\rtl\bin
```

and your CLASSPATH:

```
.;[nagjava_install_dir]\jar\NAGJava.jar
```

If you are using an IDE such as Eclipse, you may need to configure your project to enable the IDE to pick up any required dependency.

