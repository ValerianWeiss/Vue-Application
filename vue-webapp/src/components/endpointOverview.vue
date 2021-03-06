<template>
	<div id="endpointOverview">
		<div id="overviewContainer">
			<h1>{{endpointName}}</h1>
			<div id="sensorViewContainer">
				<div id="optionBar">
					<button class="btn optionBtn" @click="expandAll">
						<p>Expand all</p>
					</button>
					<button class="btn optionBtn" @click="collapseAll">
						<p>Collapse all</p>
					</button>
				</div>
				<div v-for="sensor in sensors" :key="sensor.name">
					<sensorView v-bind:sensor="sensor"
								v-on:sensorActive="onSensorActive"
								v-on:sensorInactive="onSensorInactive">
					</sensorView>
				</div>
			</div>		
		</div>
	</div>
</template>

<script lang="ts">
import Vue from 'vue';
import { Component, Prop } from 'vue-property-decorator';
import Endpoint from '../classes/Endpoint';
import { String } from 'typescript-string-operations';
import SensorView from './sensorView.vue';
import Sensor from '../classes/Sensor';

@Component({
	components: {
		SensorView,
	}
})
export default class EndpointOverview extends Vue {

	@Prop()
	private endpoint: Endpoint;
	private activeSensors: Sensor[];


	public constructor() {
		super();
		this.activeSensors = [];
	}

	private get endpointName() : string {
		if(this.endpoint == null) {
			return String.Empty;
		}
		this.activeSensors = [];
		return this.endpoint.getName();
	}

	private get sensors() : Sensor[] {
		if(this.endpoint == null) {
			return [];
		}
		return this.sortSensors(this.endpoint.getSensors());
	}
	
	private sortSensors(sensors: Sensor[]) : Sensor[] {
		
		let activeSensors: Sensor[] = [];
		let inactiveSensors: Sensor[] = [];

		for(let i = 0; i < sensors.length; i++) {
			if(this.activeSensors.indexOf(sensors[i]) >= 0) {
				activeSensors.push(sensors[i]);
			} else {
				inactiveSensors.push(sensors[i]);
			}
		}
		activeSensors = this.sortAlphabetically(activeSensors);
		inactiveSensors = this.sortAlphabetically(inactiveSensors);
		return activeSensors.concat(inactiveSensors);
	}

	private sortAlphabetically(list: Sensor[]) : Sensor[] {
        list.sort((a: Sensor, b: Sensor) : number => {
            let aName = a.getName().toLowerCase();
            let bName = b.getName().toLowerCase();
			if(aName < bName) return -1;
    		if(aName > bName) return 1;
    		return 0;
		});
		return list;
    }

	private expandAll() : void {
		this.setGraphVisibility(true);
	}

	private collapseAll() : void {
		this.setGraphVisibility(false);
	}

	private setGraphVisibility(visibility: boolean) : void {
		let sensorViews = this.$children;
		for(let i = 0; i < sensorViews.length; i++) {
			if(sensorViews[i] instanceof SensorView) {
				(sensorViews[i] as SensorView).onHeaderClick(visibility);
			}
		}
	}

	private onSensorActive(sensor: Sensor) : void {
		if(this.activeSensors.indexOf(sensor) < 0) {
			this.activeSensors.push(sensor);
		}
	}

	private onSensorInactive(sensor: Sensor) : void {
		let index = this.activeSensors.indexOf(sensor);
		if(index >= 0) {
			this.activeSensors.splice(index, 1);
		}
	}

	private beforeDestroy () {
		this.endpoint.getSensors().forEach((sensor: Sensor) => {
			this.$store.commit('unsubscribe', sensor.getTopic());
		})
	}
}
</script>

<style scoped>

#overviewContainer {
	position: relative;
	padding: 0 20px 0 20px;
    float: left;
    width: calc(100% - 40px);
	height: calc(100vh - 192px);
	overflow-y: auto;
}

#optionBar {
	width: 100%;
	height: 40px;
	background-color: #EEE;
	margin-bottom: 20px;
}

.optionBtn {
	float: left;
	margin: 5px;
	height: 30px;
	width: 120px;
	text-align: center;
	background-color: #BBB;
}

.optionBtn:hover {
	background-color: rgb(129, 179, 155);
}

</style>
