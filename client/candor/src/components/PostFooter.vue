<template>
  <div class="container"> 
    <div class="row">
      <div class="col-xl">
        {{  }}
      </div>
      <div class="col-xl">
        {{  }}
      </div>
      <div class="col-xl-2">
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
        id: {
            type: String,
            default: ''
        }, 
        edited: {
            type: String,
            default: null
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