<template>
  <div class="container"> 
    <div class="row">
      <div class="col-3 float-left">
        <b-button pill 
          variant="outline-secondary"
          @click="confirmDelete(id)"
          v-if="myPost">
          <font-awesome-icon :icon="['fas', 'trash']"></font-awesome-icon>        
        </b-button>
      </div>
      <div class="col">
        <p v-if="myPost"></p>
      </div>
      <div class="col-3">
        <b-button pill
            @click="reply(id)">
            <font-awesome-icon :icon="['fas', 'reply']"></font-awesome-icon>
        </b-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
    name: "PostFooter", 
    props: {
        user: {
          type: Number,
          default: null
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
        this.$http.post("/deletePost", null, {
          params : {
            "post" : post_id
          }
        }).then((response) => {
          this.$store.commit('deletePost', response.data);
        }).catch((error) => {
          console.log(error);
        });
      },
      reply: function(post_id) {
          const h = this.$createElement

          const message = h('div', { class: ['foobar'] }, [
              h('p', { class: ['text-center'] }, [
                  "Replies too ya ungrateful ass"
              ]),
              h('p', { class: ['text-center'] }, [h('b-spinner')])
          ])

          this.$bvModal.msgBoxOk([message], {
              buttonSize: 'sm',
              centered: true, size: 'sm',
          })
      }
    }
}
</script>
