<template>
    <div class="container"> 
        <div class="row">
            <div class="col-xl">
                {{  }}
            </div>
            <div class="col-xl">
                {{  }}
            </div>
            <div class="col-xl">
                {{ time }}
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'PostHeader',
    props: {
        // TODO: need an icon for each postHeader
        time: String,
        user: String
    }

}
</script>

<style>

</style>