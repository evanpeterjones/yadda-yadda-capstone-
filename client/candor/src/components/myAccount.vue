<template>
    <div>
        <b-modal 
            size='lg'
            title="My Account"
            hide-footer
            @hidden="hide">
            
        </b-modal>
    </div>    
</template>

<script>
export default {
    name: "myAccount", 
    mounted() {
        this.$children[0].show();
    },
    methods: {
        hide: function() {
            this.$emit('closeDialog');
        }
    }
}
</script>

<style>

</style>