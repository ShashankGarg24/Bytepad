import React, { useState, useEffect } from "react";
import { connect } from 'react-redux';

import { Modal } from "react-bootstrap";
import Navbar from "../../Components/Navbar/Navbar";
import "./LandingPage.css";
import logo from "../../assets/images/logo.png";
import logo2 from "../../assets/images/logo2.png";
import playLogo from "../../assets/images/playButton.png";
import midPhone from "../../assets/images/midPhone.png";
import sidePhone from "../../assets/images/sidePhone.png";
import si_logo from "../../assets/images/si_logo.png";
import Aos from "aos";
import * as actionCreators from '../../store/actions/actions'; 
import "aos/dist/aos.css";
import QRCode from "qrcode.react";

function LandingPage(props) {
  const [show, setShow] = useState(false);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  useEffect(() => {
    Aos.init({
      duration: 600,
    });
    props.fetchData();
  }, []);

  return (
    <>
      <Navbar />

      <div className="home">
        <div className="home_section">
          <div className="logo">
            <p>
              <img src={logo2} alt="logo" />
                Bytepad
            </p>
          </div>

          <div className="heading">
            <p>
              {" "}
              Find all previous year Question Papers <br /> with Solution in one
              place
            </p>
          </div>

          <div></div>
        </div>
      </div>

      <div className="lower_banner">
        <div className="left_text">
          <h5 style={{marginBottom: '0px'}} className="text_header">Get Bytepad on</h5>
          <h5 className="text_header"> your device</h5>
          <h6 className="small_text_head">
            Easily access all question papers and
          </h6>
          <h6 className="small_text_lower">Solutions with just one click</h6>
          <img className="playButton" src={playLogo} />
          <h6 className="small_text">Or</h6>
          <button className="qrCode" onClick={handleShow}>
            <i class="fas fa-qrcode" />
            Download from QR Code
          </button>
        </div>
        <div className="right_img">
          <img
            data-aos-delay="400"
            data-aos="fade-up"
            aos-animation
            className="firstPhone"
            src={sidePhone}
            alt="phone 1"
          />
          <img
            data-aos="fade-up"
            aos-animation
            className="secondPhone"
            src={midPhone}
            alt="phone 2"
          />
          <img
            data-aos-delay="400"
            data-aos="fade-up"
            aos-animation
            className="thirdPhone"
            src={sidePhone}
            alt="phone 3"
          />
        </div>
      </div>

      <footer>
        <span className="footer">Powered by SOFTWARE INCUBATOR</span>
        <img src={si_logo} />
      </footer>

      <Modal show={show} onHide={handleClose} centered>
        <div className="qrModal">
          <h5>Download Bytepad</h5>
          <i class="fas fa-times" onClick={handleClose}/>
          <div className='scanner'>
            <QRCode width={100} value="http://facebook.github.io/react/" />
          </div>
          <h6 className='small'>Scan the Qr code to download</h6>
          <h6 className='small'>Bytepad for android device.</h6>
        </div>
      </Modal>
    </>
  );
}

const mapStateToProps = state => {
  return {
      data: state.filter,
  }
};

const mapDispatchToProps = dispatch => {
  return {
      fetchData: () => dispatch(actionCreators.fetchDataAsync()),
  }
};

export default connect(mapStateToProps,mapDispatchToProps)(LandingPage);
