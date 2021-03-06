import chisel3._

class Sequential extends Module {
  val io = IO(new Bundle {
    val d = Input(UInt(4.W))
    val q = Output(UInt(4.W))
    val d2 = Input(UInt(4.W))
    val q2 = Output(UInt(4.W))
    val d3 = Input(UInt(4.W))
    val q3 = Output(UInt(4.W))
    val ena = Input(Bool())
    val q4 = Output(UInt(4.W))
    val q5 = Output(UInt(4.W))
    val riseIn = Input(UInt(1.W))
    val riseOut = Output(UInt(1.W))
  })

  val d = io.d
  //- start sequ_reg
  //- 开始顺序寄存器
  val q = RegNext(d)
  //- end
  //- 结束
  io.q := q

  val delayIn = io.d2
  //- start sequ_reg2
  //- 开始顺序寄存器2
  val delayReg = Reg(UInt(4.W))

  delayReg := delayIn
  //- end
  //- 结束
  io.q2 := delayReg

  val inVal = io.d3
  //- start sequ_reg_init
  //- 开始顺序寄存器初始化
  val valReg = RegInit(0.U(4.W))

  valReg := inVal
  //- end
  //- 结束
  io.q3 := valReg

  val enable = io.ena
  //- start sequ_reg_ena
  val enableReg = Reg(UInt(4.W))

  when (enable) {
    enableReg := inVal
  }
  //- end
  //- 结束
  io.q4 := enableReg

  //- start sequ_reg_init_ena
  val resetEnableReg = RegInit(0.U(4.W))

  when (enable) {
    resetEnableReg := inVal
  }
  //- end
  //- 结束
  io.q5 := resetEnableReg

  val din = io.riseIn
  //- start sequ_reg_rising
  val risingEdge = din & !RegNext(din)
  //- end
  //- 结束
  io.riseOut := risingEdge
}


class SequCounter extends Module {
  val io = IO(new Bundle {
    val out = Output(UInt(4.W))
    val event = Input(Bool())
    val eventCnt = Output(UInt(4.W))
    val tick = Output(Bool())
    val lowCnt = Output(UInt(4.W))

  })

  //- start sequ_free_counter
  //- 开始数列任意计数器
  val cntReg = RegInit(0.U(4.W))

  cntReg := cntReg + 1.U
  //- end
  //- 结束
  io.out := cntReg

  val event = io.event
  //- start sequ_event_counter
  //- 开始数列事件计数器
  val cntEventsReg = RegInit(0.U(4.W))
  when(event) {
    cntEventsReg := cntEventsReg + 1.U
  }
  //- end
  //- 结束
  io.eventCnt := cntEventsReg

  val N = 5
  //- start sequ_tick_gen
  //- 开始数列提示生成器
  val tickCounterReg = RegInit(0.U(4.W))
  val tick = tickCounterReg === (N-1).U

  tickCounterReg := tickCounterReg + 1.U
  when (tick) {
    tickCounterReg := 0.U
  }
  //- end
  //- 结束

  //- start sequ_tick_counter
  val lowFrequCntReg = RegInit(0.U(4.W))
  when (tick) {
    lowFrequCntReg := lowFrequCntReg + 1.U
  }
  //- end
  //- 结束

  io.tick := tick
  io.lowCnt := lowFrequCntReg
}