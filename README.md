Paxos Simulator
=====
A simulation package to illustrate the Paxos algorithm for consistency among nodes. It follows a 3-phase commit protocol and each node keeps track of a "log" of current changes. The commits in these logs are all consistent with each other.

---

### Set up the project in Eclipse:
* Click "New" in Eclipse
* Select "Java Project"
* Project Name: "Paxos"
* Uncheck "Use Default Location" and enter the path to this directory
* Click "Finish". Eclipse will set up the project correctly using the existing source code.
* Right click the project and click "Configure Build Path"
* Click "Add JARs..."
* Select the guava-13.0.1.jar file from the lib directory within the project
* You're done!

---

