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
                    <h4>{{ location }}</h4>
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
//import { messages } from '../en-us.js'
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
            return this.$store.getters.location;
        }, 
        'mobile' : function() {
            return this.$store.getters.isMobile;
        }
    },
    methods: {
        newPost: function(data) {
            this.$http.post("/newPost", {
                content : "test post from the client"
            }).then(response => {
                console.log(response)
                console.log('HOLY FUCKING SHIT!!!!!!!');
            }).catch(error => {
                console.log(error)
                console.log("it's alright, try again");
            });
        },
        newPostDialog: function() {
            const h = this.$createElement

            const message = h('div', { class: ['foobar'] }, [
                h('p', { class: ['text-center'] }, [
                    "I'll start allowing New Yapps soon, check back fam ;)"
                ]),
                h('p', { class: ['text-center'] }, [h('b-spinner')])
            ])

            this.$bvModal.msgBoxOk([message], {
                buttonSize: 'sm',
                centered: true, size: 'sm',
                headerClass: 'p-2 border-bottom-0',
                footerClass: 'p-2 border-top-0',
            }).then(value => {
                console.log("sending post");
                this.newPost();
            })
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
