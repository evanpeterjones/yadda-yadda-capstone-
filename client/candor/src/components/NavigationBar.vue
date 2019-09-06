<template>
<span>
    <h1> {{ appname }} </h1>
</span>
</template>
    
<script>
//import { messages } from '../en-us.js'

export default {
    name: "NavigationBar",
    data() {
        return {
            appname : 'yadda²'
        }
    }
}
</script>

<style>
span { 
    box-shadow:0 4px 10px 0 rgba(0,0,0,0.2),0 4px 20px 0 rgba(0,0,0,0.19);
    width: 100%;
    height: 20px;
    background-color: var(--primary);
}
</style>
