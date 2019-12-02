<template>
<div>
    <Feed />
    <div class="right-corner-container">     
    <b-button pill
    v-if="showingReplies"
    variant="light"
    class="button-class"
    v-b-tooltip.hover title="Close"
    @click="close(id)">
    <font-awesome-icon :icon="['fas', 'times']"></font-awesome-icon>
    </b-button>
    <br/>
    <br/>
    <b-button 
        pill
        class="button-class"
        v-b-tooltip.hover title="New Post"
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
    computed: {
        showingReplies: function() {
            return this.$store.getters.posts.length > 1;
        }
    },
    methods: {
        close: function () {
            this.$store.commit('removeReplies');
        },
        createNewPost: function() {
            this.$emit('newPostDialog')
        }
    }
}
</script>

<style>

</style>