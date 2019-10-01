<template>
  <span>
    <post-header
      :user="user"
      :location="location"
      :time="time"
    />
    <p> {{ content }} </p>
    <post-footer
      :edit="editTime"
      :location="location"
    />
  </span>
</template>

<script>
import PostHeader from './PostHeader.vue';

export default {
    name: "Post",
    props: {
        postData : {
	    type: Object,
	    default: null
	}
    },
    data () {
        return {}
    }
}
</script>

<style>

</style>
