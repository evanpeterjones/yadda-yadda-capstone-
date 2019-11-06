<template>
  <div class="feed-container">
    <div v-if="mobile">
      <div v-for="(post, index) in posts" :key="index">
        <post 
          :id="post['pst_id_pk']"
          :user="post['pst_usr_id_fk']"
          :time="post['pst_time']"
          :edited="post['pst_edittime']"
          :content="post['pst_content']"
          :decentral="post['pst_decentral']" />
      </div>
    </div>
    <div v-else style="margin:auto;width: 50%;">
      <div v-for="(post, index) in posts" :key="index">
        <post 
          :id="post['pst_id_pk']"
          :user="post['pst_usr_id_fk']"
          :time="post['pst_time']"
          :edited="post['pst_edittime']"
          :content="post['pst_content']"
          :decentral="post['pst_decentral']" />
      </div>
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
    data () {
      return {
        feedClass : {
          'margin' : 'auto',
          'width' : '50%'
        }
      }
    },
    computed: {
      posts : function() {
        return (this.$store.getters.posts[0]);
      },
      mobile : function () {
        return this.$store.getters.isMobile;
      }
    }
}
</script>

<style scoped>
.feed-container{
  height: 20px;
}
</style>