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

### Command Messages
These are messages sent from the user interface to one or more participants. Each message instructs a participant to do something, which is processed as a command rather than a decree, meaning they don't get to vote on the action, and perform it immediately upon receiving it. Unlike messages, command messages are received instantly, and are not held in a queue.

### Decrees and Decree Type
These are the things participants will eventually write in their logs. Some decrees, like a set leader, or an add or remove participant, affect the state of the participant, who then notes who is the leader or adjusts the participants list that they know of. All other decrees, dubbed "opaque" decrees, do not directly affect the state. Instead, they reflect changes in the system that the participant is aware of (but that don't affect the running of the Paxos protocol). Decrees are always sent in a Paxos Message. Depending on the type of the message, they may be voted upon or immediately written in the log. 

### Paxos Message, Message Types and Queues
All messages received by a participant are sent to a message queue, which we had to implement ourselves in order to manage the appropriate display actions. The main strategy is a priority queue with a random element determining the priority of each message as it comes in. This allows messages to get "delayed" as we model the way they pass through the network, so that not all messages will arrive in-order. The messages themselves encapsulate a Decree and a behavior. Depending on what type the decree is, participants know to consider proposing the decree, vote on the decree, or write the decree into their logs as a commit. In addition to the messages dealing with decrees, there are two more types of decrees which allow participants to request and send their log files to each other, enabling participants who have missed some communications to catch up. 

### Post Office
All messages are routed through the post office. In the effort that this project not become an exercise in networking, participants do not send messages to one another directly. Instead, they use the participants' unique id numbers to send messages via the post office, which determines in what order the messages will be put into the priority queue and provides an interface for participants to check their messages and retrieve the most recent one. In a pure paxos implementation, this would be done using network ports instead, and a centralized post office. However, it would be trivial to convert what we have into a networked implementation, and we have done this for simplicity's sake since this is only a simulation of paxos and not a paxos implementation proper.

### Paxos Log


### Participant
