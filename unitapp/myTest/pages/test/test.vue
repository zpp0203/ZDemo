<template>
	<content>
		<uni-nav-bar status-bar="true" left-text="返回" @click-left="back" :title="title" right-text="跳转" @click-right="onClickRight()"></uni-nav-bar>
		<view>{{msg}}</view>
		
		<view @click="news">新闻</view>
	</content>
</template>

<script>

	import uninavbar from '../../components/uni-nav-bar.vue'
	export default {
		//调用子组件
		components:{
			'uni-nav-bar':uninavbar 
        },
		onLoad:function(e) {
			console.debug(e.test)
			this.msg=e.test
// 			uni.setNavigationBarTitle({
// 				title:e.test,
// 			})

		},
		data() {
			return {
				title:"测试页面",
				msg:"",
			};
		},
		methods:{
			onClickRight(){
				console.debug("onClickRight")
				uni.navigateTo({/* 跳转到应用内的某个页面 */
					url: "test2?title=测试页面"
				});
			},
			back(){
				console.debug("back")
			},
			news(){
				uni.navigateTo({/* 跳转到应用内的某个页面 */
					url: "../index/index"
				});
			}
		}
	}
</script>

<style>
	@import '../../common/zpp.css';
	
</style>

