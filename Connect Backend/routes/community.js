const Community = require('../models/Community');
const express = require('express')
const router = express.Router();

const Post=require('../models/Post')

//create community Post method

router.post('/create',async(req,res,next)=>{

    const community = new Community(req.body);
    console.log(community);
    try{
        const comm = await community.save();
        const userId = req.body.userId;
        res.status(200).send({
            status:200,
            message:"community created"
        })
    }catch(err){
       res.status(400).send(err);
    }
})
router.get('/communities',async (req,res) =>{
    try{
        const communities = await Community.find();
        console.log(communities);
        res.status(200).send(communities);

    }catch(err){
        res.send(err);
    }
});

//get A specfic community
router.get('/:id',async(req,res)=>{
    const id = req.params.id;
    try{
        const community = await Community.findOne({_id:id});
        console.log(community);
        res.status(200).send(community);

    }catch(err){
        res.status(400).send(err);
    }
});

router.patch('/addmember/:id',async(req,res)=>{

console.log(res.body);
    try{

        const community = await Community.findById(req.params.id);

        if(community){

            await community.updateOne({ $push: { members: req.body._id } });

            res.status(200).send({
                status: 200,

                message: "member added",
              });
        }else{

            res.status(400);
        }
    }catch(err){

        res.send(err);
    }
});

router.get('/post/:id',async(req,res)=>{


    try{

        const posts =await Post.find({communityId : req.params.id})
        if (posts) {
            res.status(200).send(posts);
          } else {
            throw createError(404, "Not found");
          }
    }
    catch(err){

        res.send(err);
    }

});
router.get('/')

module.exports = router;