<template>
  <div class="feed-container container-fluid">
    <card v-if="!posts">
      <h5>No Yapps yet! You can be the first!^^^</h5>
    </card>
    <div class="row scrolling-wrapper">
      <div v-on:scroll="onScroll" id="feeder" v-for="feed in posts" :key="feed[0][0].pst_id_pk" class="col scrolling-vert">
        <div v-for="post in feed" :key="post.pst_id_pk">
          <post 
            :hasComments="post['pst_hascomments']"
            :parent="post['pst_parent_fk']"
            :uname="post['usr_username']"
            :id="post['pst_id_pk']"
            :user="post['pst_usr_id_fk']"
            :time="post['pst_time']"
            :edited="post['pst_edittime']"
            :content="post['pst_content']"
            :decentral="post['pst_decentral']" 
          />
        </div>
      </div>
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
    computed: {
      posts : function() {
        return (this.$store.getters.posts);
      },
      mobile : function () {
        return this.$store.getters.isMobile;
      }
    },
    methods: {
      onScroll () {
        let feeder = document.getElementById('feeder')
        let bottomOfWindow = feeder.scrollTop + feeder.offsetHeight === feeder.scrollHeight
        
        if (bottomOfWindow) {
          this.$http.get("/feed", {
            params: {
              offset: this.$store.getters.offset+5, 
              cookie: this.$cookies.get('yapp-session'), 
              loc_id: this.$store.getters.loc_id
            }
          }).then((result) => {
            console.log(result.data);
            this.$store.commit("addPosts", result.data);
            this.$store.commit('updateOffset');
          }).catch(error => {
            console.log(error);
          });
        }
      }
    }
}
</script>

<style scoped>
.feed-container{
  height: 20px;
}
.scrolling-vert {
  border: none;
  padding: 5px;
  width: 250px;
  height: 85vh;
  overflow: scroll;
  overflow-x: hidden; 
  overflow-x: auto;
}
.scrolling-wrapper {
  overflow: scroll;
}
</style>