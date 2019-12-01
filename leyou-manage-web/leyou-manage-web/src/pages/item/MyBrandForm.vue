<template>
  <v-form v-model="valid" ref="MyBrandForm">
    <v-text-field v-model="brand.name" label="请输入品牌名称" required :rules="nameRule"/>
    <v-text-field v-model="brand.letter" label="请输入品牌首字母" required :rules="letterRule" />
    <v-cascader
      url="/item/category/list"
      multiple
      required
      v-model="brand.categories"
      label="请选择商品分类"/>
    <v-layout row>
      <v-flex xs3>
        <span style="font-size: 16px; color: #444">品牌LOGO：</span>
      </v-flex>
      <v-flex>
        <v-upload
          v-model="brand.image"
          url="/upload/image"
          :multiple="false"
          :pic-width="250"
          :pic-height="90"
        />
      </v-flex>
    </v-layout>
    <v-layout class="my-4" row>
      <v-spacer/>
      <v-btn @click="submit" color="primary">提交</v-btn>
      <v-btn @click="clear" >重置</v-btn>
    </v-layout>
  </v-form>

</template>

<style>
    body {
        background-color: #ff0000;
    }
</style>

<script>
    export default {
        name:'MyBrandForm',
        props:{
          oldBrand:{
            type:Object
          },
          isEdit:{
            type: Boolean,
            default:false
          }
        },
        data() {
          return {
            valid:false,
            brand:{
              name:'',
              letter:'',
              image:'',
              categories:[]
            },
            nameRule:[
              v => !!v || "品牌名称不能为空",
              v => v.length >1 || "品牌名称至少2位"
            ],
            letterRule:[
              v => !!v || "首字母不能为空",
              v => /^[A-Z]{1}$/.test(v) || "品牌字母只能是A~Z的大写字母"
            ]
            }
        },
        created:function(){
          console.log(this)
        },
        methods:{
          submit:function(){
            let isChecked = this.$refs.MyBrandForm.validate();
            if(isChecked){
              //１.获取brand 对象给一个参数对象
              //这里用到了一个解构表达式来获取brand中的属性
              const {categories,letter,...params} = this.brand;
              //２．将categories变为字符串以逗号分隔
              params.cids = categories.map(c => c.id).join(",");
              //params 将首字母改为大写
              params.letter=letter.toUpperCase();
              //将数据提交到后台
              this.$http.post('/item/brand/save',this.$qs.stringify(params)).then(
                () => {
                  this.$message.success("保存成功！");
                  this.$emit("close");
                }
              ).catch(() => {
                this.$message.error("保存失败！");
              })
            }
          },
          clear:function(){
            this.$refs.MyBrandForm.reset();
            //分类要手动清空
            this.categories = [];
          }
        },
      watch:{
          oldBrand:{
            handler(val) {
              if(val){
                // 注意不要直接复制，否则这边的修改会影响到父组件的数据，copy属性即可
                this.brand =  Object.deepCopy(val)
              }else{
                // 为空，初始化brand
                this.brand = {
                  name: '',
                  letter: '',
                  image: '',
                  categories: [],
                }
              }
            },
            deep: true
          }
      }
    }
</script>
