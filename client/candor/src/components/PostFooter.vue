<template>
  <div class="container"> 
    <div class="row">
      <div class="col">
        {{  }}
      </div>
      <div class="col">
        <p v-if="myPost">Edit</p>
      </div>
      <div class="col">
        <b-button pill
            @click="reply(id)">
            Reply
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
        }
    },
    computed: {
      myPost: function() {
        return this.user && this.$store.getters.userId == this.user;
      }
    },
    methods: {
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

<style>

</style>