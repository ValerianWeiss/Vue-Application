import Vue from 'vue';
import Vuex from 'vuex';
import WebSocket from './classes/WebSocket';
import { String } from 'typescript-string-operations';
import Axios from 'axios';
import Config from './appConfig.json';
import * as JWT from 'jwt-decode';

Vue.use(Vuex);

const Store = new Vuex.Store({
	
	state: {
		websocket: new WebSocket(),
		heading: String.Empty,
		username: getUsername(),
	},

	getters: {
		heading: (state) : string => {
			return state.heading;
		},

		username: (state) : String | null => {	
			return state.username;			
		},

		isTokenValid: async () : Promise<boolean> => {
			let token = localStorage.getItem(Config.tokenEntity);
			if (token != null) {
				let response = await Axios.post(Config.backendUrl + '/user/isTokenValid', {token});
				return response.data;
			}
			return false;
		},
	},

	actions: {
		
	},

	mutations: {
		setHeading(state, heading: string) : void {
			state.heading = heading;
		},

		deleteToken() : void {
			localStorage.removeItem(Config.tokenEntity);
		},

		subscribe(state, subInfo: any) : void {
			state.websocket.subscribe(subInfo.topic, subInfo.callback);
		},

		unsubscribe(state, topic?: string) : void {
			state.websocket.unsubscribe(topic);
		},

		setUsername(state) : void {
			state.username = getUsername();
		}
	}
});

function getUsername() : string | null {
	let token = localStorage.getItem(Config.tokenEntity);

		if(token != null) {
			let data: any = JWT(token);
			return data["username"] as string;
		}
		return String.Empty;
}

export function getAuthHeader() : any {
	return { Authorization : "Bearer " + localStorage.getItem(Config.tokenEntity) }
}

export default Store;