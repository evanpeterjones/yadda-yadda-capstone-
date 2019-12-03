<template>
  <div>
    <b-modal 
      title="My Posts"
      scrollable
      hide-footer
      @hidden="hide"
    >
      <div v-if="!feed">
        <p> You have no posts </p>
      </div>
      <div
        v-for="post in feed"
        v-else
        :key="post.pst_id_pk"
      >
        <post @closeDialog="hide"
          :id="post['pst_id_pk']"
          :has-comments="post['pst_hascomments']"
          :parent="post['pst_parent_fk']"
          :user="post['pst_usr_id_fk']"
          :time="post['pst_time']"
          :edited="post['pst_edittime']"
          :content="post['pst_content']"
          :decentral="post['pst_decentral']"
        />
      </div>
    </b-modal>
  </div>    
</template>

<script>
import Post from './Post'

export default {
    name: "MyPosts", 
    components: {
        Post
    },
    data() {
        return { 
            feed : null
        }
    },
    mounted() {
        this.$children[0].show();

        this.$http.get("/myPosts").then((result) => {
            console.log(result.data)
            this.feed = (result.data.length == 0) ? 
                            null : result.data;
        }).catch(result => {
            console.log(result);
        });
    },
    methods: {
        hide: function() {
            this.$emit('closeDialog');
        }
    }
}
</script>
