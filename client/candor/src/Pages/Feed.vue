<template>
  <Post v-for="post in posts">

  </Post>
</template>

<script>
import Post from '../components/Post'

export default {
    name: 'Feed',
    components: [Post],
    props: {
        posts: {
            type: Array, 
            default: []
        }
    }
}
</script>

<style>

</style>