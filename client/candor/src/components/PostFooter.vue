<template>
  <div class="container"> 
    <div class="row">
      <div class="col-3 float-left">
        <b-button
          v-if="myPost" 
          v-b-tooltip.hover
          pill
          title="Delete"
          variant="outline-secondary"
          @click="confirmDelete(id)"
        >
          <font-awesome-icon :icon="['fas', 'trash']" />        
        </b-button>
      </div>
      <div class="col">
        <b-button
          v-if="hasComments"
          v-b-tooltip.hover
          pill
          title="View Comments"
          @click="comments(id)"
        >
          <font-awesome-icon :icon="['fas', 'comment']" />
        </b-button>
      </div>
      <div class="col-3">
        <b-button
          v-b-tooltip.hover
          pill
          title="Reply"
          @click="reply(id)"
        >
          <font-awesome-icon :icon="['fas', 'reply']" />
        </b-button>
      </div>
    </div>
  </div>
</template>

<script>
import { EventBus } from '../utils/event-bus.js'

export default {
    name: "PostFooter", 
    props: {
        user: {
          type: Number,
          default: null
        },
        hasComments: {
          type: Boolean,
          default: false
        },
        id: {
          type: Number,
          default: -1
        }, 
        edited: {
          type: String,
          default: null
        },
        decentral: {
          type: Boolean,
          default: false
        }
    },
    computed: {
      myPost: function() {
        return this.user && this.$store.getters.userId == this.user;
      }
    },
    methods: {
      reply: function(post_id) {
        this.$store.commit('setReplyTo', post_id)
      }, 
      confirmDelete: function (post_id) {
        const h = this.$createElement;
        const message = h('div', { class: ['foobar'] }, [ 
            h('p', { class: ['text-center'] }, [
                "Are you sure you want to delete this post?"
            ])
        ]);

        this.$bvModal.msgBoxConfirm([message], {
            buttonSize: 'sm',
            centered: true, size: 'sm',
            headerClass: 'p-2 border-bottom-0',
            footerClass: 'p-2 border-top-0',
        }).then(value => {
            if (value) {
              this.deletePost(post_id)
            }
        });
        
      },
      deletePost: function(post_id) {       
        this.$http.get("/deletePost", {
          params : {
            "post" : post_id
          }
        }).then((response) => {
          this.$store.commit('deletePost', response.data);
          console.log(response)
        }).catch((error) => {
          console.log(error);
        });
      },
      comments: function(post_id) {
        this.$http.get('/postComments', {
          params: {
            post: post_id
          }
        }).then(response => {
          console.log(response)
          this.$store.commit('addNewCommentsFeed', response.data)
        }).catch(error => {
          console.log("yeet")
          console.error(error);
        })
      }
    }
}
</script>
