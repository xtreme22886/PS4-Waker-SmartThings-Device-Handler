# PS4-Waker-SmartThings-Device-Hanlder
SmartThings Device Handler for PS4-Waker

I took this device hanlder (https://github.com/ChronoStriker1/PS4-Waker-Smartthings-Handler) and re-wrote it as things were broken with it.

Pre-Requesites
- ps4-waker w/ a REST wrapper for it. I used (https://github.com/Knapoc/ps4-waker-rest) but I made tweaks so you can find my version here (https://github.com/xtreme22886/ps4-waker-rest).

Features
- Control PS4 power (on/off)
- Control up to three (3) games (start/switch)

With this you will be able to control the PS4 power and start/switch games with the SmartThings app. You will be able to have Alexa/Google Home/Haromony Hub power on/off your playtation but cannot use voice commands to start/switch games. You can use a virtual switch in conjunction with WebCORE to have Alexa/Google Home start/switch games for you.

Note: The handler will refresh the PS4 status every 15 minutes to capture the PS4's current activity.

Installation
- Install and configure ps4-waker w/ REST wrapper on a PC / Raspberry Pi
- Make sure that a static IP address is assigned to both the PC running ps4-waker and the PS4
- Take the ps4waker.groovy code and create a new handler in your SmartThings IDE 'My Device Hanlders'
- Make sure to Save / Publish this new device handler
- Go to 'My Devices' and create a new device using this handler
- Edit the device in the smart phone app to set the Waker IP address, Waker port and the PS4 IP address

![Playing Game](https://raw.githubusercontent.com/xtreme22886/PS4-Waker-SmartThings-Device-Hanlder/master/screenshots/Playing%20Game.png)

![Playing Game](https://raw.githubusercontent.com/xtreme22886/PS4-Waker-SmartThings-Device-Hanlder/master/screenshots/Settings.png)
