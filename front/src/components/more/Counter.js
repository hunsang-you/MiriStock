import { animate } from 'framer-motion';
import React, { useEffect, useRef } from 'react';
import '../home/css/countanimation.css';

function Counter({ from, to }) {
  const nodeRef = useRef();
  useEffect(() => {
    const node = nodeRef.current;

    const controls = animate(from, to, {
      duration: 2,
      onUpdate(value) {
        node.textContent = Math.floor(value).toLocaleString();
      },
    });

    return () => controls.stop();
  }, [from, to]);

  return <span ref={nodeRef}></span>;
}

export default Counter;
