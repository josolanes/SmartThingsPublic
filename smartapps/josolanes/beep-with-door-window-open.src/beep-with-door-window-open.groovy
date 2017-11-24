/**
 *  POST Garage Door Status to solanes.us
 *
 *  Copyright 2017 Josh Solanes
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Beep with door/window open",
    namespace: "josolanes",
    author: "Josh Solanes",
    description: "Beep with door/window open",
    category: "SmartThings Labs",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Log devices...") {
        input "doors", "capability.contactSensor", title: "Doors", required:false, multiple:true
        input "beepDoors", "number", title: "Door Open Beep (1-5)", required:true, multiple:false
        input "windows", "capability.contactSensor", title: "Windows", required:false, multiple:true
        input "beepWindows", "number", title: "Window Open Beep (1-5)", required:true, multiple:false
        input "presence", "capability.presenceSensor", title: "Presence", required:false, multiple:true
        input "beepPresence", "number", title: "Arrival Beep (1-5)", required:true, multiple:false
        input "siren", "capability.tone", title: "Siren", required:true, multiple:true
    }
}

def installed() {
	log.debug "Installed with settings: ${settings}"
    log.debug "Installed with private settings: ${appSettings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
    log.debug "Updated with private settings: ${appSettings}"

	unsubscribe()
	initialize()
}

def initialize() {
	try {
        if(settings.beepDoors < 1 || settings.beepDoors > 5) {
            httpError(501, "Door Open Beep can only be between 1 and 5, $settings.beepDoors is an invalid value")
        }

        if(settings.beepWindows < 1 || settings.beepWindows > 5) {
            httpError(501, "Window Open Beep can only be between 1 and 5, $settings.beepWindows is an invalid value")
        }

        if(settings.beepPresence < 1 || settings.beepPresence > 5) {
            httpError(501, "Arrival Beep can only be between 1 and 5, $settings.beepPresence is an invalid value")
        }

        log.info("Subscribing to doors")
        subscribe(doors, "contact", handleDoorContactEvent)

        log.info("Subscribing to windows")
        subscribe(windows, "contact", handleWindowContactEvent)

        log.info("Subscribing to presence")
        subscribe(presence, "presence", handlePresenceEvent)
    }
    catch (e) {
    	log.error("Exception caught", e)
    }
}

def handleDoorContactEvent(evt) {
	log.info("--------------------------------")

	try {
        log.info("Door $evt.name $evt.value")

        if(evt.value == "open") {
            beep(settings.beepDoors)
        }
    }
    catch (e) {
    	log.error("Caught exception", e)
    }
}

def handleWindowContactEvent(evt) {
	log.info("--------------------------------")

	try {
        log.info("Window $evt.name $evt.value")

        if(evt.value == "open") {
            beep(settings.beepWindows)
        }
    }
    catch (e) {
    	log.error("Caught exception", e)
    }
}

def handlePresenceEvent(evt) {
	log.info("--------------------------------")

	try {
        log.info("Presence device $evt.name $evt.value")

        if(evt.value != "not present") {
            beep(settings.beepPresence)
        }
    }
    catch (e) {
    	log.error("Caught exception", e)
    }
}

def beep(Integer beepNum) {
	try {
        switch(beepNum) {
            case 1:
                log.info("Beeping customBeep1")

                siren.customBeep1()
                break
            case 2:
                log.info("Beeping customBeep2")

                siren.customBeep2()
                break
            case 3:
                log.info("Beeping customBeep3")

                siren.customBeep3()
                break
            case 4:
                log.info("Beeping customBeep4")

                siren.customBeep4()
                break
            case 5:
                log.info("Beeping customBeep5")

                siren.customBeep5()
                break
            default:
                httpError(501, "Invalid beep number selected ($beepNum)")
        }

        siren.stop()
    }
    catch (e) {
    	log.error("Caught exception", e)
    }
}
