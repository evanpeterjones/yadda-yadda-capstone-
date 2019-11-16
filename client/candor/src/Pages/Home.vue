<template>
<div>
    <Feed />
    <div class="right-corner-container">     
    <b-button 
        pill
        class="button-class"
        @click="createNewPost">
        <font-awesome-icon :icon="['fas', 'plus']"></font-awesome-icon>
    </b-button>
    </div>
</div>
</template>

<script>
import Feed from './Feed.vue'

export default {
    name: 'Home',
    components: {
        Feed
    },
    methods: {
        createNewPost: function() {
            this.$emit('newPostDialog')
        }
    }
}
</script>

<style>

</style>