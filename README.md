# Line-Counter
A simple program which counts the lines and words in a directory or a file or even in a git repository and classifies them based on their extensions.
This program has been written in java (version 15).

-----------
Documentation:
--
<b>Proper format for calling the program :</b></br></br>
```akolc [options] [local address/git repo address]```</br></br><b>
Options :</b>
1) ```-l``` : This option indicates that the given address
is a local address.<br><br>
2) ```-g``` : This option indicates that the given address is a git repository address.<br><br>
3) ```-h``` : This option tell the program to include hidden files and directories in the counting process.<br><br>
<b>*** Note 1 :</b> If no option is set, the program will assume that the given address is a local address, so for analyzing a
git repo you should definitely use "-g" option.<br><br>
<b>*** Note 2 :</b> ```akolc``` command can be used directly without any option or address. In that case program will ask you what you want to do.\
---
Installation:
-
You can install akolc on ubuntu or debian by downloading the debian package from this link : </br>
https://github.com/SakoDroid/Line-Counter/releases/download/1.0.0/akolc_1.0.0-1_all.deb </br>
For other distros you can install akolc by downloading the link below and extracting the archive to your root directory : </br>
https://github.com/SakoDroid/Line-Counter/releases/download/1.0.0/ako-1.0.0-linux.tar.gz