HKUST CourseReg
---
A Legacy from ancient time. Credit to author unknown. Last updated Jan 2016.

An application that finds out all possible combinations of your intended courses. 

Hopefully you should be able to run it with [Java 8](http://www.java.com/en/download/manual.jsp).

Kindly find the `.jar` application under `out/artifacts`. We've provided a latest `.ser` course information, or you can force update in the menu.

####Suggested Usage
Kick start you miserable planning by getting a feasible schedule first.

Then checkout [Student Time Table Assistant](https://w6.ab.ust.hk/jr_ta/ta_stdt_main.jsp) via myPortal, or [CourseReg with Griddy](http://raypeng.com/coursereg) for further tweak.

If you are more comfortable, if not obsessed, with fancy command line, see [PyCourseReg](https://github.com/raypeng/PyCourseReg/)


####Changelog
##### Jan 2016
1. Updated `courseInfo.ser` to Fall 2016

##### Aug 2015
1. Updated `courseInfo.ser` to Fall 2015
1. Fix a crash when COMP courses exceeds the maximum sessions supported for a dept.
1. Now `Update` will update to the latest semester. Just click the button when data is outdated. If no `courseInfo.ser` is found, it will force update.
1. Delete all the confusing past courses' data and tests.

####Maintainance
Feel free to fork. It's really a mess:).


