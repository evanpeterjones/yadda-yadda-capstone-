<template>
  <span>
    <h1>{{ appname }}</h1>
  </span>
</template>
    
<script>
//import { messages } from '../en-us.js'

export default {
    name: "NavigationBar",
    data() {
        return {
            appname : 'YAPP' //'yadda²'
        }
    }
}
</script>

<style>
@import url('https://fonts.googleapis.com/css?family=Hepta+Slab:900&display=swap');

:root { 
    --primary: gold;
}

h1{
    font-family: 'Hepta Slab', serif;
    letter-spacing: 16px;
    text-shadow: -1px -1px 0 gold,
                1px -1px 0 gold,
                -1px 1px 0 gold,
                1px 1px 0 gold;
    color: black
}
h1 span {
    box-shadow:0 4px 10px 0 rgba(0,0,0,0.2),0 4px 20px 0 rgba(0,0,0,0.19);
    width: 100%;
    height: 20px;
    background-color: var(--primary);
}
</style>
