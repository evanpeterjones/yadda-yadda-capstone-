<template>
  <div>
    <div v-if="!location" class="login">
        <h1>{{ appname[0] }}<span class="logo">{{ appname1 }}</span></h1>
    </div>
    <div class="header" v-else>
        <div v-if="!mobile">
            <div class="row">
                <div class="col">
                    <h1>{{ appname }}</h1>
                </div>
                <div class="col">
                  <b-dropdown 
                    @click="updateLocationSort"
                    split
                    pill 
                    id="dropdown-1" class="m-md-2">
                    <template v-slot:button-content>
                        <h4>{{ location }}</h4>
                    </template>
                    <b-dropdown-item>Change Location</b-dropdown-item>
                    <b-dropdown-item>My Account</b-dropdown-item>
                    <b-dropdown-item @click="showQR">Mobile Sync</b-dropdown-item>
                  </b-dropdown>
                </div>
            </div>
        </div>
        <div v-else>
            <h1>{{ appname }}</h1>
        </div>
    </div>
    <br/>
  </div>
</template>
    
<script>
import NewPost from './NewPost'

export default {
    name: "NavigationBar",
    components: {
        NewPost
    },
    data() {
        return {
            appname : 'YAPP', //'yadda yadda' //²
            appname1 : 'APP' //'yadda yadda' //²
        }
    },
    computed : {
        'location' : function() {
            var locData = this.$store.getters.locationName;
            return locData;
        },
        'mobile' : function() {
            return this.$store.getters.isMobile;
        }
    },
    methods: {
        updateLocationSort: function() {
            this.$store.commit('nextLocationType');
        },
        newPostDialog: function() {
            this.$emit('newPostDialog');
        },
        showQR: function() {
            this.$emit('newQRCode');
        }
    }
}
</script>

<style>
@import url('https://fonts.googleapis.com/css?family=Hepta+Slab:900&display=swap');

.logo {
    color: gold;
    text-shadow: 0px 0px 0 #222222;
    box-shadow:0 0px 0px 0 rgba(0,0,0,0),0 0px 0px 0 rgba(0,0,0,0);
}
h1{
    font-family: 'Hepta Slab', serif;
    letter-spacing: 16px;
    text-shadow: -1px -1px 0 gold,
                1px -1px 0 gold,
                -1px 1px 0 gold,
                1px 1px 0 gold;
    color: #222222;
    padding:10px;
}
h4{
    font-family: 'Hepta Slab', serif;
    letter-spacing: 16px;
    color: gold;
}
.header {
    box-shadow:0 4px 10px 0 rgba(0,0,0,0.2),0 4px 20px 0 rgba(0,0,0,0.19);
    width: 100%;
    background-color: #222222;    
    position:fixed; /* fixing the position takes it out of html flow - knows
                    nothing about where to locate itself except by browser
                    coordinates */
    left:0;           /* top left corner should start at leftmost spot */
    top:0;            /* top left corner should start at topmost spot */
    width:100vw;      /* take up the full browser width */
    z-index:1;  /* high z index so other content scrolls underneath */
    height:70px;     /* define height for content */
}
</style>
