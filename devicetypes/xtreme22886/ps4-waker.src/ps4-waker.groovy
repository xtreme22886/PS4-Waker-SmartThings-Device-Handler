/**
*   Copyright 2019 xtreme
* 
*   Licensed under the Apache License, Version 2.0 (the License); you may not use this file except
*   in compliance with the License. You may obtain a copy of the License at
* 
*       httpwww.apache.orglicensesLICENSE-2.0
* 
*   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
*   on an AS IS BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
*   for the specific language governing permissions and limitations under the License.
* 
*   PS4-Waker
*
*   Author xtreme
*   Date 2019-12-07 v1.0 Initial Release
**/

preferences {
    input("WakerIP", "text", title: "Waker IP", description: "Enter the IP Address of the PS4-Waker-REST Server")
    input("WakerPort", "text", title: "Waker Port", description: "Enter the TCP Port of the PS4-Waker-REST Server")
    input("PS4IP", "text", title: "PS4 IP", description: "Enter the IP Address of the Playstation 4")
    input("Game1", "text", title: "Game 1 ID", description: "Title ID of Playstation Game")
    input("Game2", "text", title: "Game 2 ID", description: "Title ID of Playstation Game")
    input("Game3", "text", title: "Game 3 ID", description: "Title ID of Playstation Game")
}  
 
metadata {
    definition (name: "ps4-waker", namespace: "xtreme22886", author: "xtreme") {
        capability "Actuator"
        capability "Switch"
        capability "Refresh"
        
        attribute "nowPlaying", "string"
        attribute "gameID", "string"
        
        command "on"
        command "off"
        command "refresh"
        command "game1"
        command "game2"
        command "game3"
}

tiles(scale: 2) {
    multiAttributeTile(name:"switch", type: "generic", width: 6, height: 4, canChangeIcon: false){
        tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
            attributeState "on", label:'${name}', action:"off", icon: "https://raw.githubusercontent.com/xtreme22886/PS4-Waker-SmartThings-Device-Hanlder/master/icons/PS_Color.png", backgroundColor: "#00a0dc", nextState: "turningOff"
            attributeState "off", label:'${name}', action:"on", icon: "https://raw.githubusercontent.com/xtreme22886/PS4-Waker-SmartThings-Device-Hanlder/master/icons/PS_Grey.png", backgroundColor: "#ffffff", nextState: "turningOn"
            attributeState "turningOn", label:'${name}', icon: "https://raw.githubusercontent.com/xtreme22886/PS4-Waker-SmartThings-Device-Hanlder/master/icons/PS_Grey.png", backgroundColor: "#00a0dc", nextState: "turningOff"
            attributeState "turningOff", label:'${name}', icon: "https://raw.githubusercontent.com/xtreme22886/PS4-Waker-SmartThings-Device-Hanlder/master/icons/PS_Color.png", backgroundColor: "#ffffff", nextState: "turningOn"
        }
        tileAttribute("device.nowPlaying", key: "SECONDARY_CONTROL") {
    		attributeState("default", label:'Current Activity: ${currentValue}', icon: "https://raw.githubusercontent.com/xtreme22886/PS4-Waker-SmartThings-Device-Hanlder/master/icons/gameicon.png")
        }
    }
    standardTile("on", "device.on", width: 2, height: 2, decoration: "flat") {
            state "default", label:"On", action:"on", icon:"https://raw.githubusercontent.com/xtreme22886/PS4-Waker-SmartThings-Device-Hanlder/master/icons/PS4_On.png"
        }   
    standardTile("off", "device.off", width: 2, height: 2, decoration: "flat") {
            state "default", label:"Off", action:"off", icon:"https://raw.githubusercontent.com/xtreme22886/PS4-Waker-SmartThings-Device-Hanlder/master/icons/PS4_Off.png"
        }
    standardTile("refresh", "device.refresh", width: 2, height: 2, decoration: "flat") {
            state "default", label:"", action:"refresh", icon:"https://raw.githubusercontent.com/xtreme22886/PS4-Waker-SmartThings-Device-Handler/master/icons/refresh.png"
        }
    standardTile("game1", "device.game1", width: 2, height: 2, decoration: "flat") {
    		state "default", label:"Game 1", action:"game1", backgroundColor:"#ffffff"
        }
    standardTile("game2", "device.game2", width: 2, height: 2, decoration: "flat") {
    		state "default", label:"Game 2", action:"game2", backgroundColor:"#ffffff"
        }
    standardTile("game3", "device.game3", width: 2, height: 2, decoration: "flat") {
    		state "default", label:"Game 3", action:"game3", backgroundColor:"#ffffff"
        }
    valueTile("label_gameID", "", width: 2, height: 1, decoration: "flat") {
            state "default", label:'Title ID', backgroundColor:"#ffffff"
        }
    valueTile("gameID", "device.gameID", width: 4, height: 1, decoration: "flat") {
        	state "default", label:'${currentValue}', backgroundColor:"#ffffff"
    }
        main "switch"
        details(["switch","on","off","refresh","game1","game2","game3","label_gameID","gameID"])
    }
}

def updated() {
    configure()
    refresh()
}

def installed() {
    configure()
}

def configure() {
    runEvery15Minutes(refresh)
}

def parse(String description) {
    log.debug "Parsing '${description}'"
}

def setCurrentGame(String playing, String titleID){
    sendEvent(name: "nowPlaying", value: playing, descriptionText: "Current PS4 activity: ${playing}")
    sendEvent(name: "gameID", value: titleID, descriptionText: "Current GameID: ${titleID}")
}

def refresh(){
    try {
	log.debug "Executing Refresh"
        def cmd = makeCommand("info")
        sendCommand(cmd, callback)
        }
    	catch (Exception e)
    	{
       		log.debug "Hit Exception $e"
    	}
}

def on() {
    try {
    	log.debug "Executing On"
    	def cmd = makeCommand("on")
    	sendCommand(cmd, null)
        sendEvent(name: "switch", value: "on", descriptionText: "$device.displayName is on", isStateChange: "true")
        setCurrentGame("PS4 Home Screen", null)
    	}
    	catch (Exception e)
    	{
        	log.debug "Hit Exception $e"
    	}
}

def off() {
    try {
    	log.debug "Executing Off"
    	def cmd = makeCommand("off")
    	sendCommand(cmd, null)
        sendEvent(name: "switch", value: "off", descriptionText: "$device.displayName is off", isStateChange: "true")
        setCurrentGame("PS4 Rest Mode", null)
    	}
    	catch (Exception e)
    	{
        	log.debug "Hit Exception $e"
    	}
}

def callback(physicalgraph.device.HubResponse hubResponse){
    def msg
    try {
        msg = parseLanMessage(hubResponse.description)
		def jsonObj = new groovy.json.JsonSlurper().parseText(msg.body)
        def status = jsonObj.status
        def titleID = jsonObj.'running-app-titleid'
        def playing
    	if (status == "Ok"){
        	playing = jsonObj.'running-app-name'
            if(playing){
            	setCurrentGame(playing, titleID)
            } else {
            	playing = "PS4 Home Screen"
            	setCurrentGame(playing, titleID)
            }
    	} else if (status == "Standby"){
            playing = "PS4 Rest Mode"
            setCurrentGame(playing, titleID)
    	} else {
        	status = "Redo"
    	}
    	log.debug ("Status: ${status}")
        log.debug ("Current Game: ${playing}")
        log.debug ("Current TitleID: ${titleID}")
    } catch (e) {
        log.error "Exception caught while parsing data: "+e;
    }
}

def game1() {
    log.debug "Starting Game 1"
    def headers = [:]
    headers.put("HOST","${settings.WakerIP}:${settings.WakerPort}")
    def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/ps4/${settings.PS4IP}/start/${settings.Game1}",
        headers: headers
    )
    if (device.currentValue("switch") == "off"){
        sendEvent(name: "switch", value: "on", descriptionText: "$device.displayName is on", isStateChange: "true")
        }
    setCurrentGame("Starting Game 1", settings.Game1)
    return result
}

def game2() {
    log.debug "Starting Game 2"
    def headers = [:]
    headers.put("HOST","${settings.WakerIP}:${settings.WakerPort}")
    def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/ps4/${settings.PS4IP}/start/${settings.Game2}",
        headers: headers
    )
    if (device.currentValue("switch") == "off"){
        sendEvent(name: "switch", value: "on", descriptionText: "$device.displayName is on", isStateChange: "true")
        }
    setCurrentGame("Starting Game 2", settings.Game2)
    return result
}

def game3() {
    log.debug "Starting Game 3"
    def headers = [:]
    headers.put("HOST","${settings.WakerIP}:${settings.WakerPort}")
    def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/ps4/${settings.PS4IP}/start/${settings.Game3}",
        headers: headers
    )
    if (device.currentValue("switch") == "off"){
        sendEvent(name: "switch", value: "on", descriptionText: "$device.displayName is on", isStateChange: "true")
        }
    setCurrentGame("Starting Game 3", settings.Game3)
    return result
}

def sendCommand(options, _callback){
    def myhubAction = new physicalgraph.device.HubAction(options, null, [callback: _callback])
    sendHubCommand(myhubAction)
}

def makeCommand(cmd){
    def options = [
     	"method": "GET",
        "path": "/ps4/${settings.PS4IP}/${cmd}",
        "headers": [
        	"HOST": "${settings.WakerIP}:${settings.WakerPort}",
            "Content-Type": "application/json"
        ]
    ]
    return options
}
