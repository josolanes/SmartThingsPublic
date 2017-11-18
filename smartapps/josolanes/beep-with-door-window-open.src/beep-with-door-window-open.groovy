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
        input "contact", "capability.contactSensor", title: "Contact", required:true, multiple: true
        input "siren", "capability.tone", title: "Siren", required:true, multiple: true
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
	subscribe(contact, "contact", handleEvent)
}

def handleEvent(evt) {
if(evt.value == "open")
    {
    	siren.customBeep1()
        siren.stop()
    }
}