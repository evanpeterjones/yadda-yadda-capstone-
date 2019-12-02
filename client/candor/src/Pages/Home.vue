<template>
  <div>
    <Feed />
    <div class="right-corner-container">     
      <b-button
        v-if="showingReplies"
        v-b-tooltip.hover
        pill
        variant="light"
        class="button-class"
        title="Close"
        @click="close(id)"
      >
        <font-awesome-icon :icon="['fas', 'times']" />
      </b-button>
      <br>
      <br>
      <b-button 
        v-b-tooltip.hover
        pill
        class="button-class"
        title="New Post"
        @click="createNewPost"
      >
        <font-awesome-icon :icon="['fas', 'plus']" />
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