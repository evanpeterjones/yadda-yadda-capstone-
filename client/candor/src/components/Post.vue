<template>
  <span>
    <post-header
      :user="postData.user"
      :location="postData.location"
      :time="postData.time"
    />
    <p> {{ content }} </p>
    <post-footer
      :edit="postData.editTime"
      :location="postData.location"
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
    }
}
</script>

<style>

</style>
