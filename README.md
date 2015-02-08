# RX-GUIDE
RXGUIDE is a tablet based application for tablets running on Android OS. It enables a patient to register himself at a clinic and also enables a doctor to write prescriptions for registered patients, print the prescriptions and also to view previous prescriptions of registered patients.

To run the app on an android device, just copy the file RXGUIDE.apk present in the RXGUIDE folder on this CD and install it on your device.

To view the app on a computer/laptop, ensure that you have a suitable development environment like Eclipse and the Android SDK bundles installed.
To install them on a linux system, follow the below instructions.

Step 1:

Check in System Settings -> Details, whether your Ubuntu is 32-bit or 64-bit

Step 2:

(Using software centre or command lines) If your Ubuntu is a 32-bit OS then install libgl1-mesa-dev 
In case of 64-bit OS install ia32-libs

Step 3: Install openjdk-6-jdk or better, openjdk-7-jdk

Step 4: Download the Android SDK or the ADT Bundle from http://developer.android.com/sdk/index.html#download and unzip it to wherever you want.

Step 5: Check that the unzipped ADT Bundle folder (or Android SDK folder, as the case may be) have the folders tools and platform-tools. These folders contain some important commands stored. Export them. Exporting them can be done as follows:

First execute sudo gedit ~/.bashrc. A file opens. Just add these lines to that file:
th
export PATH=$PATH:/path/to/tools
export PATH=$PATH:/path/to/platform-tools
Here you have to replace /path/to/tools and /path/to/platform-tools by the absolute paths according to where you unzipped the SDK or the ADT bundle download. Now all the commands adb, android, emulator etc can be simply executed in the terminal (without need to give it with absolute paths). That is, you will not get a "command not found" error.

Once you have successfully installed the development environment, import the folder RXGUIDE as an existing code into the workspace and you can view the code for the application.

If you wish to see the server side implementation, please install web2py by downloading the source from http://web2py.com/init/default/download. You will get a zip file.
Unzip it in any location of your choice. Copy the Server_source folder to web2py/applications.
Run the file web2py/web2py.py and once the server starts, open http://127.0.0.1:8000/admin/default/design/Server_source in your browser to see the Server Design
