<template>
  <div class="feed-container">
    <card v-if="!posts">
      <h5>No Yapps yet! You can be the first!^^^</h5>
    </card>
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
</template>

<script>
import Post from '../components/Post.vue'
import Card from '../components/Card.vue'

export default {
    name: 'Feed',
    components: {
      Post, Card
    },
    data () {
      return {
        feedClass : {
          'margin' : 'auto',
          'width' : '50%'
        }
      }
    },
    methods: {
      onScroll () {
        let bottomOfWindow = window.innerHeight+document.documentElement.scrollTop == document.documentElement.scrollHeight

        if (bottomOfWindow) {
          this.$http.get("/feed", {
            params: {
              offset: this.$store.getters.offset+5
            }
          }).then((result) => {
            this.$store.commit("addPosts", result.data);
            this.$store.commit('updateOffset');
            console.log(result.data);
          });
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
    },
    created () {
      window.addEventListener('scroll', this.onScroll);
    },
    destroyed () {
      window.removeEventListener('scroll', this.onScroll);
    }
}
</script>

<style scoped>
.feed-container{
  height: 20px;
}
</style>