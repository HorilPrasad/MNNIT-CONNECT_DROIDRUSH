const Community = require('../models/Community');
const express = require('express')
const router = express.Router();

//create community Post method

router.post('/create',async(req,res,next)=>{

    const community = new Community(req.body);
    try{
        const comm = await community.save();
        const userId = req.body.userId;
        res.status(200).send({
            status:200,
            message:"community created"
        })
    }catch(err){
        next(err);
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
router.get('/')

module.exports = router;