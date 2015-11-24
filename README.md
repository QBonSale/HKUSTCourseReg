HKUST CourseReg
---
A Legacy from ancient time. Credit to author unknown. Last updated Aug 2015

An java application that finds out all combinations of your intended courses. 
Hopefully you should be able to run it with Java 8.

####Suggested Usage
Kick start you miserable planning by getting a feasible schedule first.

Then checkout [Student Time Table Assistant](https://w6.ab.ust.hk/jr_ta/ta_stdt_main.jsp) via myPortal, or [CourseReg with Griddy](http://raypeng.github.io/) for further tweak.

If you are more comfortable, if not obsessed, with fancy command line, see [PyCourseReg](https://github.com/raypeng/PyCourseReg/)


####Changelog
1. Updated `courseInfo.ser` to Fall 2015
1. Fix a crash when COMP courses exceeds the maximum sessions supported for a dept.
1. Now `Update` will update to the latest semester. Just click the button when data is outdated. If no `courseInfo.ser` is found, it will force update.
1. Delete all the confusing past courses' data and tests.
1. Apply various changes to the code and migrates to Java 8, (for -> foreach,  anonymous classes -> lambda). Thanks to Intellij.

####Maintainance
Feel free to fork. It's really a mess:).
