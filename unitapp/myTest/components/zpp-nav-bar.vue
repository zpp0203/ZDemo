<template>
	<div>
		<view class="navigationbar">--{{title}}--</view>
	</div>
</template>

<script>
	export default {
		/* 接收父组件值 */
		props: {
            title: {
                type: String,
                default: '标题'
            }
        },
		
		data() {
			return {
				
			};
		}
	}
</script>

<style>
	.navigationbar{
		width: 100%;
		height: 100upx;
		padding-top: var(--status-bar-height);
		justify-content: center;/* 里面的水平居中 */
		align-items: center;/* 垂直居中 */
		background: #8A6DE9;
	}
</style>
