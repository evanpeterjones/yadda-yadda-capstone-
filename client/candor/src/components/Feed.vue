<template>
  <div>
    <p v-for='post in posts' v-bind:key="post">
        {{ post }}
    </p>
  </div>
</template>

<script>
export default {
    name: 'Feed',
    computed: {
        posts : function() {
            return (this.$store.getters.posts);
        }
    }, 
    mounted() {
        console.log(this.posts[0])
    }
}
</script>

<style>

</style>