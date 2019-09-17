<template>
  <span>
    <post-header :user=user :location=location :time=time></post-header>
    <p> {{content}} </p>
    <post-footer :edit=editTime :location=location></post-footer>
  </span>
</template>

<script>
import PostHeader from './PostHeader.vue';

export default {
    name: "Post",
    props: {
        postData : {
            user : String,
            content : String,
            location : String,
            time : String,
            editTime : String
        }
    },
    data () {
        return {}
    }
}
</script>

<style>

</style>