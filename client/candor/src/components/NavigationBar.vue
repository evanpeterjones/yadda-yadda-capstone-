<template>
  <div>
    <div v-if="!location" class="login">
        <h1>{{ appname[0] }}<span class="logo">{{ appname1 }}</span></h1>
    </div>
    <div class="container" v-else>
        <div v-if="!mobile">
            <div class="row">
                <div class="col">
                    <h1>{{ appname }}</h1>
                </div>
                <div class="col">
                    <b-button pill @click="newPostDialog">New Yapp 📣</b-button>
                </div>
                <div class="col">
                  <b-dropdown pill id="dropdown-1" class="m-md-2">
                    <template v-slot:button-content>
                        <h4>{{ location }}</h4>
                    </template>
                    <b-dropdown-item >Change Location</b-dropdown-item>
                    <b-dropdown-item>My Account</b-dropdown-item>
                  </b-dropdown>
                </div>
            </div>
        </div>
        <div v-else>
            <div class="row">
                <div class="col">
                    <h1>{{ appname }}</h1>
                </div>
                <div class="col">
                    <b-button pill @click="newPostDialog">📣</b-button>
                </div>
            </div>
        </div>
    </div>
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
            var k = this.$store.getters.location[0];
            if (k) {
                console.log(k);
                return k['loc_alias'];
            }
            return ''
        },
        'mobile' : function() {
            return this.$store.getters.isMobile;
        }
    },
    methods: {
        newPostDialog: function() {
            this.$emit('newPostDialog');
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
    color: #222222
}
h4{
    font-family: 'Hepta Slab', serif;
    letter-spacing: 16px;
    color: gold;
}
h1 span {
    box-shadow:0 4px 10px 0 rgba(0,0,0,0.2),0 4px 20px 0 rgba(0,0,0,0.19);
    width: 100%;
    height: 20px;
    background-color: #222222;
}
</style>
