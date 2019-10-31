<template>
  <div>
    <div v-for="(post, index) in posts" :key="index">
      <post 
        :id="post['pst_id_pk']"
        :user="post['pst_usr_id_fk']"
        :time="post['pst_time']"
        :edited="post['pst_edittime']"
        :content="post['pst_content']"
        :decentral="post['pst_decentral']"></post>
    </div>
  </div>
</template>

<script>
import Post from '../components/Post.vue'

export default {
    name: 'Feed',
    components: {
      Post
    },
    computed: {
      posts : function() {
        return (this.$store.getters.posts[0]);
      }
    }, 
    mounted() {
      console.log(this.posts)
    }
}
</script>

<style>

</style>